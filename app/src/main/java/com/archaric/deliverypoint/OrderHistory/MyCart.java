package com.archaric.deliverypoint.OrderHistory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.ChangeDrawerInterface;
import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.IndividualRestaurant.IndividualResCategoryModel;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.LoginSignUp.Login;
import com.archaric.deliverypoint.MainActivity;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.SendCartItemsTotal;
import com.archaric.deliverypoint.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyCart extends Fragment implements SendCartItemsTotal, SendAddressPosition {
    public static final String PLACE_ORDER_DATA = "PlaceOrderData";
    CartAdapter cartAdapter;
    RecyclerView cartListRec;
    TextView cartCount, placeOrder, changeAddress, totalCartAmount, cusName, deliveryTo, buildingNoAndName, roomNoAndBlockAndCity, cusArea, mobileAndLane;
    CardView noDataFoundLayout, noInternetFoundLayout;
    FrameLayout addressChanger;
    int pos = 0;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout addressLayout;

    OrdersModel ordersModel;
    Float cartTotal;

    public MyCart() {
        // Required empty public constructor
    }

    private BroadcastReceiver addressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String json = intent.getStringExtra("ADDRESS") ;
            Gson gson = new Gson();
            //onBackPressed();
            //sheet.dismiss();
            //String json = sharedPrefs.getString("TAG", "[]");
            Type type = new TypeToken<OrdersModel>() {}.getType();
            OrdersModel address = gson.fromJson(json, type);
            plotData(address);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        addressLayout  = view.findViewById(R.id.addressLayout);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        cartAdapter = new CartAdapter(this);
        cartCount = view.findViewById(R.id.cartCount);
        totalCartAmount = view.findViewById(R.id.totalCartAmount);
        placeOrder = view.findViewById(R.id.placeOrder);
        cartListRec = view.findViewById(R.id.cartListRec);
        changeAddress = view.findViewById(R.id.changeAddress);
        noDataFoundLayout  = view.findViewById(R.id.noDataFoundLayout);
        noInternetFoundLayout = view.findViewById(R.id.noInternetFoundLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cartListRec.setLayoutManager(layoutManager);
        addressChanger = view.findViewById(R.id.addressChanger);
        cusName = view.findViewById(R.id.cusName);
        deliveryTo = view.findViewById(R.id.deliveryTo);
        buildingNoAndName = view.findViewById(R.id.buildingNoAndName);
        roomNoAndBlockAndCity = view.findViewById(R.id.roomNoAndBlockAndCity);
        cusArea = view.findViewById(R.id.cusArea);
        mobileAndLane = view.findViewById(R.id.mobileAndLane);

        // Inflate the layout for this fragment
        return view;
    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        if (Utils.userData(getActivity())!= null) {
            endPoint.getUserAddress(Utils.userData(getActivity()).getId()).enqueue(new Callback<List<OrdersModel>>() {
                @Override
                public void onResponse(Call<List<OrdersModel>> call, Response<List<OrdersModel>> response) {
                    ArrayList<OrdersModel> ordersModels = (ArrayList<OrdersModel>) response.body();
                    if (response.isSuccessful()){
                        if (ordersModels.size() != 0) {
                            plotData(ordersModels.get(0));
                            addressLayout.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }else {
                            addressLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrdersModel>> call, Throwable t) {

                }
            });
        }else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }

    }

    private void plotData(OrdersModel ordersModels) {
        this.ordersModel = ordersModels;
        cusName.setText(ordersModels.getCustomername());
        deliveryTo.setText(ordersModels.getDeliveryto()+ ",") ;
        buildingNoAndName.setText("Building no : "+ordersModels.getBuildingno() + ", "
                +"Building name : "+ ordersModels.getBuildingName()+ ",");
        roomNoAndBlockAndCity.setText("Room no : "+ordersModels.getRoomno() + ", "
                +"Block no : "+ordersModels.getBlockno() + ", "
                +"Street no : "+ ordersModels.getStreetno() + ", "
                + ordersModels.getArea()+ ",");
        cusArea.setText(ordersModels.getArea());
        mobileAndLane.setText(ordersModels.getPhoneno() + ", " + ordersModels.getLaneLineNO());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Utils.isNetworkOnline(getActivity())) {
            noInternetFoundLayout.setVisibility(View.VISIBLE);
            cartCount.setText("");
        }
        String addressStr = Utils.getStoredData(getContext(), "ADDRESS");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(addressReceiver, new IntentFilter("ADDRESS"));
        if(!addressStr.isEmpty()){
            Gson gson = new Gson();

            //String json = sharedPrefs.getString("TAG", "[]");
            Type type = new TypeToken<OrdersModel>() {}.getType();
            OrdersModel address = gson.fromJson(addressStr, type);


            //itemsArrayList.add(item);
            //json = gson.toJson(itemsArrayList);
            plotData(address);
            addressLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        } else {
            getData();
        }




        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressChanger.setVisibility(View.VISIBLE);
               // placeOrder.setVisibility(View.INVISIBLE);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.addressChanger,new ChangeAddressAtCart());
//                transaction.commit();
                new ChangeAddressAtCart().show(getActivity().getSupportFragmentManager(),"CartAddress");
            }
        });






        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Utils.userData(getContext()) != null) {
                        if (addressLayout.getVisibility() == View.VISIBLE){
                            if (!Utils.userData(getContext()).getId().isEmpty()) {
                                System.out.println(Utils.userData(getContext()).getId());
                                Calendar cal = Calendar.getInstance();
                                long today = cal.getTime().getTime();
                                Format f = new SimpleDateFormat("dd/MM/yy");
                                String strDate = f.format(new Date());
                                System.out.println(today + strDate);

                                if (ordersModel != null){
                                    ordersModel.setDate(String.valueOf(today));
                                    ordersModel.setDatef(strDate);
                                    ordersModel.setSubtotal(cartTotal);





                                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    Gson gson = new Gson();
                                    String json = sharedPrefs.getString("TAG", "[]");
                                    Type type = new TypeToken<List<Items>>() {}.getType();
                                    ArrayList<Items> arrayList = gson.fromJson(json, type);

                                    System.out.println(arrayList);

                                    if (arrayList != null){
                                        ordersModel.setItems(arrayList);
                                        String resId = arrayList.get(0).getBid();
                                        setDataOfDeliveryFee(resId);
                                        ordersModel.setRid(resId);
                                    }
                                }
                            }
                        }else {Utils.toast(getActivity(),"Add Address details in settings");}
                }else {
                    startActivity(new Intent(getActivity(), Login.class));
                }






            }
        });

       try {
           SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
           Gson gson = new Gson();
           String json = sharedPrefs.getString("TAG", "[]");
           Type type = new TypeToken<List<Items>>() {}.getType();
           ArrayList<Items> arrayList = gson.fromJson(json, type);


        if (Utils.isNetworkOnline(getActivity())) {
            cartCount.setText("("+arrayList.size()+")");
        }

        if (arrayList.size() == 0) {
            cartCount.setText("");
            noDataFoundLayout.setVisibility(View.VISIBLE);
        }


        cartAdapter.setItemsArrayList(arrayList);
        cartListRec.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

       }catch (Exception e){

           e.printStackTrace();
       }

    }

    private void setDataOfDeliveryFee(String rid){
        System.out.println(rid + "inside**************************************************************");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getRes(rid).enqueue(new Callback<FiftyPercentOfferModel>() {
            @Override
            public void onResponse(Call<FiftyPercentOfferModel> call, Response<FiftyPercentOfferModel> response) {
                FiftyPercentOfferModel offerModel = response.body();
                if (offerModel != null) {
                    System.out.println(offerModel.getDelivery_fee()+ "**************************************************************");
                    ordersModel.setDeliveryfee(Float.valueOf(String.valueOf(offerModel.getDelivery_fee())));
                    setPlaceOrder();
                }
            }

            @Override
            public void onFailure(Call<FiftyPercentOfferModel> call, Throwable t) {

            }
        });
    }

    private void setPlaceOrder(){

        System.out.println("Working********************************");

        SendPlaceOrderData sendPlaceOrderData;


        try {
            sendPlaceOrderData = (SendPlaceOrderData) getActivity();
        }catch (Exception e){
            e.printStackTrace();
            sendPlaceOrderData = null;

        }

        if (sendPlaceOrderData != null){
            sendPlaceOrderData.toPlaceOrderData("setPaymentMethod" , ordersModel);
        }


//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.putExtra(PLACE_ORDER_DATA,ordersModel);
//            getActivity().startActivity(intent);


    }


    @Override
    public void cartTotal(Float total, int cartCount1) {
        totalCartAmount.setText("Total KD " + String.valueOf(total));
        cartTotal = total;
        cartCount.setText(String.valueOf("("+ cartCount1 + ")"));
        if (cartCount1 == 0) {
            cartCount.setText("");
            noDataFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendAddressPosition(int position) {
        //plotData(ordersModelsHere,position);
    }
}