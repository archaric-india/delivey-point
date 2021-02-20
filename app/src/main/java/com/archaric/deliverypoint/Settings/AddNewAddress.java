package com.archaric.deliverypoint.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.AddressModel;
import com.archaric.deliverypoint.Fragments.LocationPicker;
import com.archaric.deliverypoint.IndividualRestaurant.IndividualResCategoryModel;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.google.gson.Gson;

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

import static com.archaric.deliverypoint.IndividualRestaurant.ListOfItemsIndividualResAdapter.ITEM_ID_DATA;
import static com.archaric.deliverypoint.Settings.AddressesAdapter.ADDRESS_DATA;


public class AddNewAddress extends AppCompatActivity implements ShareLocationAddress {

    LinearLayout backToHomePageOnTitle, getMyCurrentLocation, houseLayout, apartmentLayout, officeLayout;
    AddressDataInterface addressDataInterface;
    TextView titleOfPage, houseTv, apartmentTv, officeTv, wholeAddress, wholeAddressTitle, buildingNo, saveAddress;
    ImageView houseOkIV, houseIv, apartmentOkIV, apartmentIv, officeOkIV, officeIv;
    String title;
    FrameLayout setLocationPickerFragment;
    EditText customerCity;
    String deliveryTo;
    Double lat = 0.00, lng = 0.00;

    EditText customer_name, mobileNumber, laneLineNumber,
            cusCity, blockNo, avenue,
            roomNo, jaddha, addressTitle, streetNum, streetName, buildingName, buildingNum, addDirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        init();

        Intent myIntent = getIntent(); // gets the previously created intent
        String firstKeyName = myIntent.getStringExtra("AddAddress");
        if (!TextUtils.isEmpty(firstKeyName)) {
            title = firstKeyName;
        }

        if (!TextUtils.isEmpty(title)) {
            titleOfPage.setText(title);
            if (title.equals("Add a New Address")) {
                getMyCurrentLocation.setVisibility(View.VISIBLE);
            }else {
                getMyCurrentLocation.setVisibility(View.GONE);
            }

        }

        OrdersModel ordersModel = (OrdersModel) getIntent().getSerializableExtra(ADDRESS_DATA);
        if (ordersModel != null) {

            plotData(ordersModel);

        }



        getMyCurrentLocation.setOnClickListener(v -> {
            setLocationPickerFragment.setVisibility(View.VISIBLE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.setLocationPickerFragment,new LocationPicker(AddNewAddress.this));
            transaction.commit();
        });

        houseLayout.setOnClickListener(v -> {
            deliveryTo = "House";
            houseLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_darkline));
            houseOkIV.setVisibility(View.VISIBLE);
            houseIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            houseTv.setTextColor(getResources().getColor(R.color.black));

            apartmentLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_review));
            apartmentOkIV.setVisibility(View.GONE);
            apartmentIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.line)));
            apartmentTv.setTextColor(getResources().getColor(R.color.line));

            officeLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_review));
            officeOkIV.setVisibility(View.GONE);
            officeIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.line)));
            officeTv.setTextColor(getResources().getColor(R.color.line));


        });

        apartmentLayout.setOnClickListener(v -> {
            deliveryTo = "Apartment";
            apartmentLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_darkline));
            apartmentOkIV.setVisibility(View.VISIBLE);
            apartmentIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            apartmentTv.setTextColor(getResources().getColor(R.color.black));

            houseLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_review));
            houseOkIV.setVisibility(View.GONE);
            houseIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.line)));
            houseTv.setTextColor(getResources().getColor(R.color.line));

            officeLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_review));
            officeOkIV.setVisibility(View.GONE);
            officeIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.line)));
            officeTv.setTextColor(getResources().getColor(R.color.line));
        });

        officeLayout.setOnClickListener(v -> {
            deliveryTo = "Office";
            officeLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_darkline));
            officeOkIV.setVisibility(View.VISIBLE);
            officeIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            officeTv.setTextColor(getResources().getColor(R.color.black));

            houseLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_review));
            houseOkIV.setVisibility(View.GONE);
            houseIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.line)));
            houseTv.setTextColor(getResources().getColor(R.color.line));

            apartmentLayout.setBackground(getResources().getDrawable(R.drawable.bg_tv_review));
            apartmentOkIV.setVisibility(View.GONE);
            apartmentIv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.line)));
            apartmentTv.setTextColor(getResources().getColor(R.color.line));

        });

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    validateData();


            }
        });

        backToHomePageOnTitle.setOnClickListener(v -> onBackPressed());
    }

    private void plotData(OrdersModel ordersModel) {
        customer_name.setText(ordersModel.getCustomername());
        mobileNumber.setText(ordersModel.getPhoneno());
        laneLineNumber.setText(ordersModel.getLaneLineNO());
        cusCity.setText(ordersModel.getArea());
        blockNo.setText(ordersModel.getBlockno());
        avenue.setText(ordersModel.getStreetName());
        roomNo.setText(ordersModel.getRoomno());
        jaddha.setText(ordersModel.getJedda());
        addressTitle.setText("");
        streetNum.setText(ordersModel.getStreetno());
        streetName.setText(ordersModel.getStreetName());
        buildingName.setText(ordersModel.getBuildingName());
        buildingNum.setText(ordersModel.getBuildingno());
        addDirect.setText(ordersModel.getDescription());
        avenue.setText(ordersModel.getAvenue());
        addressTitle.setText(ordersModel.getAddressTitle());
        if (!ordersModel.getAddress().isEmpty()){
            wholeAddressTitle.setVisibility(View.VISIBLE);
            wholeAddress.setVisibility(View.VISIBLE);
            wholeAddress.setText(ordersModel.getAddress());
        }
    }

    private void init(){

        buildingNo  = findViewById(R.id.buildingNo);
        wholeAddressTitle = findViewById(R.id.wholeAddressTitle);
        customerCity = findViewById(R.id.customerCity);
        wholeAddress = findViewById(R.id.wholeAddress);
        setLocationPickerFragment = findViewById(R.id.setLocationPickerFragment);
        getMyCurrentLocation = findViewById(R.id.getMyCurrentLocation);
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        titleOfPage = findViewById(R.id.titleOfPage);
        houseLayout = findViewById(R.id.houseLayout);
        apartmentLayout = findViewById(R.id.apartmentLayout);
        houseTv = findViewById(R.id.houseTv);
        apartmentTv = findViewById(R.id.apartmentTv);
        houseOkIV = findViewById(R.id.houseOkIV);
        houseIv = findViewById(R.id.houseIv);
        apartmentOkIV = findViewById(R.id.apartmentOkIV);
        apartmentIv = findViewById(R.id.apartmentIv);
        officeLayout = findViewById(R.id.officeLayout);
        officeTv = findViewById(R.id.officeTv);
        officeOkIV = findViewById(R.id.officeOkIV);
        officeIv = findViewById(R.id.officeIv);

        customer_name= findViewById(R.id.customer_name);
        mobileNumber= findViewById(R.id.mobileNumber);
        laneLineNumber= findViewById(R.id.laneLineNumber);
                cusCity= findViewById(R.id.cusArea);
                blockNo= findViewById(R.id.blockNo);
                avenue= findViewById(R.id.avenue);
                roomNo= findViewById(R.id.roomNo);
                jaddha= findViewById(R.id.jaddha);
                addressTitle= findViewById(R.id.addressTitle);
        streetNum = findViewById(R.id.streetNum);
                streetName= findViewById(R.id.streetName);
                buildingName= findViewById(R.id.buildingName);
                buildingNum= findViewById(R.id.buildingNum);
                addDirect = findViewById(R.id.addDirect);

        saveAddress = findViewById(R.id.saveAddress);

    }

    private void validateData(){

        String cusName =   customer_name.getText().toString();
        String mobNum =   mobileNumber.getText().toString();
        String LLNum =   laneLineNumber.getText().toString();
        String CCity =   cusCity.getText().toString();
        String Bno =   blockNo.getText().toString();
        String av =   avenue.getText().toString();
        String Rno =   roomNo.getText().toString();
        String jad =   jaddha.getText().toString();
        String addressT =   addressTitle.getText().toString();
        String sName =   streetName.getText().toString();
        String bName =   buildingName.getText().toString();
        String bNum =   buildingNum.getText().toString();
        String addDis =   addDirect.getText().toString();
        String sno = streetNum.getText().toString();

        if (TextUtils.isEmpty(cusName)) {
            Utils.toast(this,"Enter Your Name");
            return;
        }
        if (TextUtils.isEmpty(mobNum)) {
            Utils.toast(this,"Enter Your Mobile Number");
            return;
        }

        if (TextUtils.isEmpty(CCity)) {
            Utils.toast(this,"Enter Your City");
            return;
        }
        if (TextUtils.isEmpty(Bno)) {
            Utils.toast(this,"Enter Your Block Number");
            return;
        }
        if (TextUtils.isEmpty(Rno)) {
            Utils.toast(this,"Enter Your Room Number");
            return;
        }
        if (TextUtils.isEmpty(sno)) {
            Utils.toast(this,"Enter Your Street No");
            return;
        }
        if (TextUtils.isEmpty(sName)) {
            Utils.toast(this,"Enter Your Street Name");
            return;
        }
        if (TextUtils.isEmpty(bName)) {
            Utils.toast(this,"Enter Your Building Name");
            return;
        }
        if (TextUtils.isEmpty(bNum)) {
            Utils.toast(this,"Enter Your Building Number");
            return;
        }

        if (wholeAddress.getVisibility() == View.VISIBLE){
            wholeAddress.getText().toString();
        }

        if (TextUtils.isEmpty(LLNum)){
            LLNum = "null";
        }

        if (TextUtils.isEmpty(av)){
            av = "null";
        }

        if (TextUtils.isEmpty(jad)){
            jad = "null";
        }

        if (TextUtils.isEmpty(addressT)){
            addressT = "null";
        }

        if (TextUtils.isEmpty(addDis)){
            addDis = "null";
        }




//        Calendar cal = Calendar.getInstance();
//        long today = cal.getTime().getTime();
//        Format f = new SimpleDateFormat("dd/MM/yy");
//        String strDate = f.format(new Date());
//        System.out.println(today + strDate);



        if (title.equals("Add a New Address")){
            OrdersModel ordersModel = new OrdersModel();
            ordersModel.setId("");
            ordersModel.setType("");
            ordersModel.setDeliveryto(deliveryTo);
            ordersModel.setBlockno(blockNo.getText().toString());
            ordersModel.setStreetno(streetNum.getText().toString());
            ordersModel.setBuildingno(buildingNum.getText().toString());
            ordersModel.setRoomno(roomNo.getText().toString());
            ordersModel.setJedda(jad);
            ordersModel.setArea(cusCity.getText().toString());
            ordersModel.setPhoneno(mobileNumber.getText().toString());
            ordersModel.setDescription(addDis);
//            ordersModel.setDate(String.valueOf(today));
//            ordersModel.setDatef(strDate);
            ordersModel.setAvenue(av);
            ordersModel.setBuildingName(buildingName.getTag().toString());
            ordersModel.setStreetName(streetName.getText().toString());
            ordersModel.setLaneLineNO(LLNum);
            ordersModel.setUid(Utils.userData(this).getId());
            ordersModel.setCustomername(customer_name.getText().toString());
            ordersModel.setAddressTitle(addressT);
            if (wholeAddress.getVisibility() == View.VISIBLE) {
                ordersModel.setAddress(wholeAddress.getText().toString());
            }else {
                ordersModel.setAddress("null");
            }

            com.archaric.deliverypoint.location location = new com.archaric.deliverypoint.location();
            location.setLat(lat);
            location.setLong(lng);
            ordersModel.setLocation(location);

            System.out.println(ordersModel);
            Gson gson = new Gson();
            String json = gson.toJson(ordersModel);
            System.out.println(json);

            addAddressWork(json);
        }


    }

    private void addAddressWork(String json) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.setUserAddress(json).enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                if (response.isSuccessful()) {
                    Utils.toast(AddNewAddress.this,"Address Added!");
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                Utils.toast(AddNewAddress.this,"Address Added!");
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    public void locationDetails(ArrayList<AddressModel> addressModel) {
        System.out.println(addressModel.get(0).getPincode() + addressModel.get(0).getWholeAddress());

        wholeAddressTitle.setVisibility(View.VISIBLE);
        wholeAddress.setVisibility(View.VISIBLE);
        wholeAddress.setText(addressModel.get(0).getWholeAddress());
        cusCity.setText(addressModel.get(0).getLocality());
        buildingNum.setText(addressModel.get(0).getBuildingNo());
        lat = addressModel.get(0).getLat();
        lng = addressModel.get(0).getLng();
    }
}