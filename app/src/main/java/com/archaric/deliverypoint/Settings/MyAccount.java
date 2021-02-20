package com.archaric.deliverypoint.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.R;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

public class MyAccount extends AppCompatActivity {

    TextView edit, done, profilePictureChange, textViewName, textViewPhoneNO, textViewEmail;
    EditText editTextName, editTextPhoneNO, editTextEmail;
    LinearLayout backToHomePageOnTitle, parentRelative;
    ImageView profilePicture;

    private static final int CAMERA_CAPTURE_REQUEST = 101;
    private static final int CAMERA_PERMISSION_REQUEST = 102;
    public static final int SETTING_INTENT_CODE = 201;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

       init();

        permissionCheck();
        edit.setOnClickListener(v -> {
            edit.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
            profilePictureChange.setVisibility(View.VISIBLE);
            textViewName.setVisibility(View.GONE);
            textViewPhoneNO.setVisibility(View.GONE);
            textViewEmail.setVisibility(View.GONE);

            editTextName.setVisibility(View.VISIBLE);
            editTextPhoneNO.setVisibility(View.VISIBLE);
            editTextEmail.setVisibility(View.VISIBLE);




        });

        done.setOnClickListener(v -> {
            edit.setVisibility(View.VISIBLE);
            done.setVisibility(View.GONE);
            profilePictureChange.setVisibility(View.INVISIBLE);
            textViewName.setVisibility(View.VISIBLE);
            textViewPhoneNO.setVisibility(View.VISIBLE);
            textViewEmail.setVisibility(View.VISIBLE);

            editTextName.setVisibility(View.GONE);
            editTextPhoneNO.setVisibility(View.GONE);
            editTextEmail.setVisibility(View.GONE);

        });

        backToHomePageOnTitle.setOnClickListener(v -> {
            if (done.getVisibility() == View.VISIBLE){
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            onBackPressed();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            onBackPressed();
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Save Changes?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }else {
                onBackPressed();
            }
        });



        profilePictureChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermission();
                doThis(v);
            }
        });



    }

    private void init(){
        edit = findViewById(R.id.Edit);
        done = findViewById(R.id.Done);
        profilePictureChange = findViewById(R.id.profilePictureChange);
        profilePicture = findViewById(R.id.profilePicture);
        textViewName= findViewById(R.id.textViewName);
        textViewPhoneNO= findViewById(R.id.textViewPhoneNO);
        textViewEmail= findViewById(R.id.textViewEmail);
        editTextName= findViewById(R.id.editTextName);
        editTextPhoneNO= findViewById(R.id.editTextPhoneNO);
        editTextEmail = findViewById(R.id.editTextEmail);
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        parentRelative = findViewById(R.id.parentRelative);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showSnackBar(){
        Snackbar.make(parentRelative,"This Application needs Camera Permission",Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent,SETTING_INTENT_CODE);
                    }
                }).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handlePermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showSnackBar();
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST);
            }

        }
    }

    private void openCamera() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (data != null){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                filePath = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    profilePicture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        switch (requestCode){
            case CAMERA_CAPTURE_REQUEST:

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
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissionCheck(){
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
    }


    public void doThis(View view) {
        CropImage.activity().start(MyAccount.this);
    }
}