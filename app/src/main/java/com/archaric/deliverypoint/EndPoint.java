package com.archaric.deliverypoint;

import com.archaric.deliverypoint.Fragments.AllCategoriesModel;
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.IndividualRestaurant.IndividualResCategoryModel;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.IndividualRestaurant.OverAllRatingModel;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.Coupon;
import com.archaric.deliverypoint.OrderHistory.DeliveryModel;
import com.archaric.deliverypoint.OrderHistory.OrderDetailsModel;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.OrderHistory.ServerResponse;
import com.archaric.deliverypoint.Settings.NotificationModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface EndPoint {


    @GET("getcategories")
    Call<List<AllCategoriesModel>> getAllCategories();

    @GET("getspecialoffers")
    Call<List<AllCategoriesModel>> getSpecialOffers(@Query("zoneid") String zoneid);

    @GET("get50percentoff")
    Call<List<FiftyPercentOfferModel>> getFiftyOffers(@Query("zoneid") String zoneid);

    @GET("getrestuarantaroundyou")
    Call<List<FiftyPercentOfferModel>> getResAroundYou(@Query("lat") double lat, @Query("lng") double lng, @Query("zoneid") String zoneid);

    @GET("getsearchresuser")
    Call<List<FiftyPercentOfferModel>> searchHome(@Query("key") String key, @Query("zoneid") String zoneid);

    @GET("getnewlyjoinedres")
    Call<List<FiftyPercentOfferModel>> getNewlyJoined(@Query("zoneid") String zoneid);


    @GET("getrescategories")
    Call<List<IndividualResCategoryModel>> getResCategory(@Query("bid") String id);

    @GET("getresratings")
    Call<OverAllRatingModel> getResRating(@Query("bid") String id);


    @GET("getsearchitemuser")
    Call <List<Items>> getSearchItems(@Query("bid") String id, @Query("key") String key);

    @GET("getuserorders")
    Call <List<OrdersModel>> getOrderDetails(@Query("uid") String uid);

    @GET("signupuser")
    Call <UserModel> setSignUp(@Query("name") String name , @Query("phone") String phone , @Query("mail") String mail , @Query("pass") String pass);

    @GET("loginuser")
    Call <UserModel> getLoginUser(@Query("mail") String mail , @Query("pass") String pass);

    @GET("getuseraddress")
    Call <List<OrdersModel>> getUserAddress(@Query("uid") String uid);

    @GET("addaddress")
    Call <OrdersModel> setUserAddress(@Query("address") String address);

    @GET("placeorder")
    Call <OrdersModel> setPlaceOrder(@Query("order") String order);

    @GET("getresdetails")
    Call <FiftyPercentOfferModel> getRes(@Query("id") String id);

    @GET("updatewallet")
    Call <ServerResponse> updateWallet(@Query("uid") String uid, @Query("wallet") float wallet);

    @GET("applycoupon")
    Call <Coupon> getCoupons(@Query("code") String code, @Query("uid") String uid);

    @GET("editaddress")
    Call <ServerResponse> editUserAddress(@Query("address") String address);

    @GET("removeaddress")
    Call <ServerResponse> removeUserAddress(@Query("addressid") String addressid);

    @GET("updatestatus")
    Call <ServerResponse> orderCancel(@Query("oid") String oid, @Query("status") String status);

    @GET("getzoneid")
    Call <ZoneModel> getCustomerZone(@Query("lat") double lat, @Query("long") double lng);


    @GET("getdeliveryDetails")
    Call <DeliveryModel> getDeliveryDetails(@Query("id") String id);

    @GET("uploadimage")
    Call <ServerResponse> sendImage(@Query("str") String str);

    @GET("getusernotifications")
    Call <NotificationModel> getNotification(@Query("uid") String uid);

    @Multipart
    @POST("uploadimagefile")
    Call<ServerResponse> postAvatar(@Part MultipartBody.Part file);

}
