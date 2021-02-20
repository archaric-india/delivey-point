package com.archaric.deliverypoint.OrderHistory;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.SetPlaceOrderData;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Mapset.ZONE_ID;


public class OrderTracking extends Fragment implements PopupMenu.OnMenuItemClickListener {

    ImageView closeIVToGoBack, moreOptions, driverImage;
    OrdersModel ordersModel;
    TextView orderNo, expTime, totItems, totAmt, orderStatus, expTime2, orderNo2,
            orderDate, deliveryFee, totAmt2, orderStatus2, wholeAddress, area, driverName, ratingLen;
    RatingBar ratingBarOfDriver;
    ImageView callToDriver;
    DeliveryModel deliveryModel;


    public OrderTracking() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_tracking, container, false);

        ordersModel = SetPlaceOrderData.getOrdersModel();

        callToDriver = view.findViewById(R.id.callToDriver);
        ratingLen = view.findViewById(R.id.ratingCount);
        ratingBarOfDriver = view.findViewById(R.id.ratingBarOfDriver);
        driverImage = view.findViewById(R.id.driverImage);
        driverName = view.findViewById(R.id.driverName);

        orderNo = view.findViewById(R.id.orderNo);
        expTime = view.findViewById(R.id.expTime);
        totItems = view.findViewById(R.id.totItems);
        totAmt = view.findViewById(R.id.totAmt);
        orderStatus = view.findViewById(R.id.orderStatus);
        expTime2 = view.findViewById(R.id.expTime2);
        orderNo2 = view.findViewById(R.id.orderNo2);
        orderDate = view.findViewById(R.id.orderDate);
        totAmt2 = view.findViewById(R.id.totAmt2);
        deliveryFee = view.findViewById(R.id.deliveryFee);
        orderStatus2 = view.findViewById(R.id.orderStatus2);
        wholeAddress = view.findViewById(R.id.wholeAddress);
        area = view.findViewById(R.id.area);



        closeIVToGoBack = view.findViewById(R.id.closeIVToGoBack);
        moreOptions = view.findViewById(R.id.moreOptions);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ordersModel != null){
            System.out.println(ordersModel + "In Order Tracking");
            plotData(ordersModel);
            getDriverDetails(ordersModel);
        }


        closeIVToGoBack.setOnClickListener(v -> getActivity().onBackPressed());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mapViewFragment,new MapsFragment());
        transaction.commit();

        callToDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(getActivity(),deliveryModel.getPhone().toString());
            }
        });

        moreOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getActivity(), v, Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
            popup.inflate(R.menu.popup_menu);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String json = sharedPrefs.getString("Lang", "en");
            System.out.println(json + "HERE");
            if (json.equals("en")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popup.setGravity(Gravity.END);
                    popup.setOnMenuItemClickListener(this);
                    popup.setGravity(Gravity.RIGHT);
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popup.setGravity(Gravity.START);
                    popup.setOnMenuItemClickListener(this);
                    popup.setGravity(Gravity.END);
                }
            }


            popup.show();
        });
    }

    private void plotData(OrdersModel ordersModel) {
        orderNo.setText("#"+ordersModel.getId());
        expTime.setText(String.valueOf(ordersModel.getExptime()) + " Mins");
        List<Items> items = ordersModel.getItems();
        totItems.setText(String.valueOf(items.size() + " Items"));
        totAmt.setText(String.valueOf("KWD "+ordersModel.getTotal()));
        orderStatus.setText(ordersModel.getStatus());
        expTime2.setText(String.valueOf(ordersModel.getExptime()));
        orderNo2.setText("#"+ordersModel.getId());
                orderDate.setText("Order Date : " + ordersModel.getDatef());
                deliveryFee.setText("Delivery Fee : " + String.valueOf(ordersModel.getDeliveryfee()));
                totAmt2.setText(String.valueOf("Total Amount : KWD " + ordersModel.getTotal()));
                orderStatus2.setText(ordersModel.getMethod());
                wholeAddress.setText(ordersModel.getAddress());
                area.setText(ordersModel.getArea());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                // do your code
                return true;
            case R.id.cancel_order:

            String sts = ordersModel.getStatus().toString();
            if (sts.equals("Out For Delivery")){
                final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                View view1 = getLayoutInflater().inflate(R.layout.dialog_for_cancel_order, null);
                TextView closeWindow = view1.findViewById(R.id.closeWindow);
                alert.setView(view1);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                closeWindow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }else {
                if (ordersModel != null){
                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            orderCancel(ordersModel.getId());
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to cancel this order?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                }
            }
                return true;
            default:
                return false;
        }
    }

    private void orderCancel(String oid){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.orderCancel(oid,"Cancelled").enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Utils.toast(getActivity(),"Order Cancelled!");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Utils.toast(getActivity(),t.getMessage().toString());
            }
        });
    }

    private void getDriverDetails(OrdersModel ordersModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String zoneId = Utils.getStoredData(getActivity(), ZONE_ID);
        System.out.println(zoneId + "Zone Id at Special Offers");

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getDeliveryDetails(ordersModel.getAssignedto()).enqueue(new Callback<DeliveryModel>() {
            @Override
            public void onResponse(Call<DeliveryModel> call, Response<DeliveryModel> response) {
                if (response.isSuccessful()) {
                    DeliveryModel Model = response.body();
                    if (Model != null) {
                        setDeliveryBoyDetails(Model);
                        deliveryModel = Model;
                        System.out.println(Model);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryModel> call, Throwable t) {

            }
        });
    }

    private void setDeliveryBoyDetails(DeliveryModel deliveryBoyDetails){

        if (deliveryBoyDetails.getRatinglen().equals("0")) {
            ratingLen.setText("(0)");
        }

        ratingBarOfDriver.setRating(deliveryBoyDetails.getRating());
        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(getActivity())
                .load(deliveryBoyDetails.getProfile())
                .apply(reqOpt)
                .into(driverImage);
        driverName.setText(deliveryBoyDetails.getName());
    }
}