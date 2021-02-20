package com.archaric.deliverypoint.Settings;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.Fragments.AddressModel;
import com.archaric.deliverypoint.Fragments.LocationPicker;
import com.archaric.deliverypoint.R;

import java.util.ArrayList;


public class AddAndEditAddresses extends Fragment implements ShareLocationAddress {

    LinearLayout backToHomePageOnTitle, getMyCurrentLocation, houseLayout, apartmentLayout, officeLayout;
    AddressDataInterface addressDataInterface;
    TextView titleOfPage, houseTv, apartmentTv, officeTv;
    ImageView houseOkIV, houseIv, apartmentOkIV, apartmentIv, officeOkIV, officeIv;
    String title;

    LocationPicker locationPicker;

    public AddAndEditAddresses(String s) {
        if (!TextUtils.isEmpty(s)) {
            title = s;

        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_and_edit_addresses, container, false);

        init(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void init(View view){
        getMyCurrentLocation = view.findViewById(R.id.getMyCurrentLocation);
        backToHomePageOnTitle = view.findViewById(R.id.backToHomePageOnTitle);
        titleOfPage = view.findViewById(R.id.titleOfPage);
        houseLayout = view.findViewById(R.id.houseLayout);
        apartmentLayout = view.findViewById(R.id.apartmentLayout);
        houseTv = view.findViewById(R.id.houseTv);
        apartmentTv = view.findViewById(R.id.apartmentTv);
        houseOkIV = view.findViewById(R.id.houseOkIV);
        houseIv = view.findViewById(R.id.houseIv);
        apartmentOkIV = view.findViewById(R.id.apartmentOkIV);
        apartmentIv = view.findViewById(R.id.apartmentIv);
        officeLayout = view.findViewById(R.id.officeLayout);
        officeTv = view.findViewById(R.id.officeTv);
        officeOkIV = view.findViewById(R.id.officeOkIV);
        officeIv = view.findViewById(R.id.officeIv);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!TextUtils.isEmpty(title)) {
            titleOfPage.setText(title);
            if (title.equals("Add a New Address")) {
                getMyCurrentLocation.setVisibility(View.VISIBLE);
            }else {
                getMyCurrentLocation.setVisibility(View.GONE);
            }
        }

        getMyCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addressDataInterface = (AddressDataInterface) getActivity();
                    addressDataInterface.AddressIntentData("LocationPicker");
                }catch (Exception e){
                    addressDataInterface = null;
                    e.printStackTrace();
                }
            }
        });

        houseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


            }
        });

        apartmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        officeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });

        backToHomePageOnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addressDataInterface = (AddressDataInterface) getActivity();
                    addressDataInterface.AddressIntentData("MyAddresses");
                }catch (Exception e){
                    addressDataInterface = null;
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void locationDetails(ArrayList<AddressModel> addressModel) {
        System.out.println(addressModel.get(0).getPincode());
    }
}