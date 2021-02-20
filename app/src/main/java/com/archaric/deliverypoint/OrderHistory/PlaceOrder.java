package com.archaric.deliverypoint.OrderHistory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.SetPlaceOrderData;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.LoginSignUp.Login;
import com.archaric.deliverypoint.LoginSignUp.SignUp;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.MainActivity;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.LoginSignUp.SignUp.USER_MODEL;

public class PlaceOrder extends Fragment {


    TextView insufficientCoupon, placeOrderFinally, totalPurchaseAmount, applyCoupon;
    RadioButton cp, cod, knet;
    RadioGroup rbGroup;
    Boolean creditPointsEligible = true, couponEligible = false, cashOnDelivery = false, cashOnDeliveryOnly = false;
    float total;
    int offer;
    String couponCodeFromDatabase;
    EditText couponCode;


    public PlaceOrder() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        cod = view.findViewById(R.id.cod);
        knet  = view.findViewById(R.id.knet);
        applyCoupon = view.findViewById(R.id.applyCoupon);
        couponCode = view.findViewById(R.id.couponCode);
        totalPurchaseAmount = view.findViewById(R.id.totalPurchaseAmount);
        insufficientCoupon = view.findViewById(R.id.insufficientCoupon);
        placeOrderFinally = view.findViewById(R.id.placeOrderFinally);
        rbGroup = view.findViewById(R.id.rbGroup);
        cp = view.findViewById(R.id.cp);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       OrdersModel ordersModel =  SetPlaceOrderData.getOrdersModel();
        if (ordersModel != null) {
           // placeOrder(ordersModel);
            total =  ordersModel.getSubtotal() + ordersModel.getDeliveryfee();
            System.out.println("sub" + ordersModel.getSubtotal() + "User Wallet" + Utils.userData(getActivity()).getWallet());
            plotData(ordersModel);
        }

        cp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Float subTotal =  ordersModel.getSubtotal();
                    if (Utils.userData(getActivity()).getWallet() < total){
                        insufficientCoupon.setVisibility(View.VISIBLE);
                        insufficientCoupon.setText("! You don't have balance to pay. \nPlease choose another payment method");
                        creditPointsEligible = false;
                        cashOnDelivery = false;
                    }else {
                        insufficientCoupon.setVisibility(View.GONE);
                        creditPointsEligible = true;
                    }
                }
            }
        });

        cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (insufficientCoupon.getVisibility() == View.VISIBLE){
                        insufficientCoupon.setVisibility(View.GONE);
                    }
                    cashOnDelivery = true;
                    creditPointsEligible = false;
                    Utils.toast(getContext(),"Working!");
                }
            }
        });

        knet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Float subTotal =  ordersModel.getSubtotal();

                }
            }
        });

        placeOrderFinally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(cp.isChecked()){
                    if (creditPointsEligible){
                        if (!TextUtils.isEmpty(couponCode.getText().toString()) && !couponEligible){
                            insufficientCoupon.setText("Try with another coupon!");
                            insufficientCoupon.setVisibility(View.VISIBLE);
                        }else {
                            float remain = Utils.userData(getActivity()).getWallet()  - total;
                            ordersModel.setTotal(remain);
                            ordersModel.setMethod("Credit Points");
                            System.out.println(remain + "Remaining Amt");
                            placeOrderByCreditPoints(ordersModel, remain);
                        }


                    }
                    if (creditPointsEligible && couponEligible){
                        if (!TextUtils.isEmpty(couponCode.getText().toString())) {
                            if (couponCode.getText().toString().trim().equals(couponCodeFromDatabase)) {
                                float couponValue = (float) calculatePercentage(offer,total);
                                System.out.println(couponValue + "Coupon Value");
                                float tot = total - couponValue;
                                float remain = Utils.userData(getActivity()).getWallet() - tot;
                                ordersModel.setTotal(tot);
                                ordersModel.setMethod("Credit Points");
                                System.out.println(tot + "tot Value" + remain + "Remain");
                                placeOrderByCreditPoints(ordersModel, remain);
                            }
                        }


                    }
                }

                if (cod.isChecked()){
                    if (insufficientCoupon.getVisibility() == View.VISIBLE){
                        insufficientCoupon.setVisibility(View.GONE);
                    }
                    cashOnDelivery = true;
                    creditPointsEligible = false;
                    Utils.toast(getContext(),"Working!");
                     if (!couponEligible){
                         cashOnDeliveryOnly = true;
                     }

                    if (cashOnDeliveryOnly){
                        if (!TextUtils.isEmpty(couponCode.getText().toString()) && !couponEligible){
                            insufficientCoupon.setText("Try with another coupon!");
                            insufficientCoupon.setVisibility(View.VISIBLE);

                        }else {
                            ordersModel.setTotal(total);
                            ordersModel.setMethod("cash");
                            placeOrder(ordersModel);
                        }

                    }

                    if (cashOnDelivery && couponEligible){
                        if (!TextUtils.isEmpty(couponCode.getText().toString())) {
                            if (couponCode.getText().toString().trim().equals(couponCodeFromDatabase)) {
                                System.out.println(offer +"" +total);
                                float couponValue = (float) calculatePercentage(offer,total);
                                System.out.println("Coupons Calculated "+ couponValue);
                                ordersModel.setMethod("cash");
                                float tot = total - couponValue;
                                ordersModel.setTotal(tot);
                                placeOrder(ordersModel);
                            }
                        }



                    }
                }




            }
        });

        applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(couponCode.getText().toString())) {
                    System.out.println(couponCode.getText().toString());
                    checkCoupon(couponCode.getText().toString(), ordersModel);
                }else {
                    Utils.toast(getActivity(), "Enter Valid Coupon!");
                }
            }
        });

    }

    public double calculatePercentage(float obtained, float total) {
        return ((total) * (obtained / 100));
    }

    private void checkCoupon(String coupon, OrdersModel ordersModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (Utils.userData(getActivity()) != null) {
            EndPoint endPoint = retrofit.create(EndPoint.class);
            System.out.println(Utils.userData(getActivity()).getId());
            endPoint.getCoupons(coupon,Utils.userData(getActivity()).getId()).enqueue(new Callback<Coupon>() {
                @Override
                public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                    if (response.isSuccessful()) {
                        Coupon coupon1 = response.body();
                        System.out.println(coupon1);
                        if (coupon1 != null) {
                            if (coupon1.getId() != null) {
                                couponEligible = true;
                                System.out.println(couponEligible);
                                couponCodeFromDatabase = coupon1.getCode();
                                insufficientCoupon.setText("Coupon Eligible");
                                insufficientCoupon.setVisibility(View.VISIBLE);
                                offer = coupon1.getOffer();
                                System.out.println(coupon1);
                                ordersModel.setCoupon(coupon1);
                            }else  if(coupon1.getCode().equals("Already Used")) {
                                couponEligible = false;
                                insufficientCoupon.setVisibility(View.VISIBLE);
                                insufficientCoupon.setText("Coupon already used.");
                            }else {
                                couponEligible = false;
                                insufficientCoupon.setVisibility(View.VISIBLE);
                                insufficientCoupon.setText("Coupon Not Eligible, try with another one.");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Coupon> call, Throwable t) {
                        t.printStackTrace();
                }
            });
        }
    }

    private void plotData(OrdersModel ordersModel) {

        cp.setText("Credit Point \nUse your KWD " + Utils.userData(getActivity()).getWallet() + " Delivery money");
        totalPurchaseAmount.setText("Total KWD "+String.valueOf(total));

    }

    private void placeOrderByCreditPoints(OrdersModel ordersModel, float remain) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (Utils.userData(getActivity()) != null) {
            EndPoint endPoint = retrofit.create(EndPoint.class);
            endPoint.updateWallet(ordersModel.getUid(), remain)
                    .enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    Utils.toast(getActivity(),response.body().response.toString());
                    ordersModel.setMethod("Credit Points");
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    UserModel userModel = Utils.userData(getActivity());
                    userModel.setWallet(remain);
                    String json = gson.toJson(userModel);
                    editor.putString(USER_MODEL, json);
                    editor.commit();
                    placeOrder(ordersModel);
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Utils.toast(getActivity(),t.getMessage().toString());

                }
            });
        }



    }

    private void placeOrder(OrdersModel ordersModel){
        ordersModel.setStatus("Order Placed!");
        Gson gson = new Gson();
        String json = gson.toJson(ordersModel);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("Before Sending Data"+json);

        if (Utils.userData(getActivity()) != null) {
            EndPoint endPoint = retrofit.create(EndPoint.class);
            endPoint.setPlaceOrder(json).enqueue(new Callback<OrdersModel>() {
                @Override
                public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson2 = new Gson();
                        String json2 = sharedPrefs.getString("TAG", "[]");
                        ArrayList<OrdersModel> models = new ArrayList<>();
                        json2 = gson2.toJson(models);
                        editor.putString("TAG", json2);
                        System.out.println(json2);
                        editor.commit();
                        System.out.println("Response Data" + response.body());
                        Utils.toast(getActivity(),"Order Placed! Go to Order History page for more details.");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<OrdersModel> call, Throwable t) {
                    Utils.toast(getActivity(), t.getMessage().toString());

                }
            });
        }
    }



}