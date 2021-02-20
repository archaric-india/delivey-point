package com.archaric.deliverypoint.OrderHistory;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.google.android.material.snackbar.Snackbar;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class WriteAReview extends Fragment {


    LinearLayout backToHomePageOnTitle, submitAReview;
    RelativeLayout parentRelative;
    CardView addPhotos;
    ImageView reviewImageView;
    RatingBar OrRatingBAR,QrRatingBAR,DrRatingBAR,VrRatingBAR;
    private static final int CAMERA_CAPTURE_REQUEST = 101;
    private static final int CAMERA_PERMISSION_REQUEST = 102;
    public static final int SETTING_INTENT_CODE = 201;


    public WriteAReview() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_a_review, container, false);

        backToHomePageOnTitle = view.findViewById(R.id.backToHomePageOnTitle);
        addPhotos = view.findViewById(R.id.addPhotos);
        parentRelative = view.findViewById(R.id.parentRelative);
        reviewImageView = view.findViewById(R.id.reviewImageView);
        submitAReview = view.findViewById(R.id.submitAReview);
        OrRatingBAR= view.findViewById(R.id.OrRatingBAR);
        QrRatingBAR = view.findViewById(R.id.QrRatingBAR);
        DrRatingBAR = view.findViewById(R.id.DrRatingBAR);
        VrRatingBAR = view.findViewById(R.id.VrRatingBAR);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backToHomePageOnTitle.setOnClickListener(v -> getActivity().onBackPressed());
        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCheck();
                handlePermission();
            }
        });

        submitAReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllData();
            }
        });



    }

    private void checkAllData() {
        float OrRateValue = OrRatingBAR.getRating();
        float QrRateValue = QrRatingBAR.getRating();
        float DrRateValue = DrRatingBAR.getRating();
        float VrRateValue = VrRatingBAR.getRating();
        if (OrRateValue == 0.0){
            Utils.toast(getActivity(),"Give Order Rating");
            return;
        }
        if (QrRateValue == 0.0){
            Utils.toast(getActivity(),"Give Quality Rating");
            return;
        }
        if (DrRateValue == 0.0){
            Utils.toast(getActivity(),"Give Delivery Rating");
            return;
        }
        if (VrRateValue == 0.0){
            Utils.toast(getActivity(),"Give Value for Money Rating");
        }


    }

    private void permissionCheck(){
        if (checkSelfPermission(getActivity(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_CAPTURE_REQUEST);
    }

    private void showSnackBar(){
        Snackbar.make(parentRelative,"This Application needs Camera Permission",Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        startActivityForResult(intent,SETTING_INTENT_CODE);
                    }
                }).show();

    }

    private void handlePermission(){
        if (checkSelfPermission(getActivity(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            openCamera();
        }else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showSnackBar();
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_REQUEST:
                if (resultCode == Activity.RESULT_OK)
                {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    reviewImageView.setVisibility(View.VISIBLE);
                    reviewImageView.setImageBitmap(photo);
                }
                break;

            case SETTING_INTENT_CODE:
                handlePermission();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }
}