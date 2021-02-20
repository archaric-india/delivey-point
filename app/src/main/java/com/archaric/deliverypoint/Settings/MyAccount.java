package com.archaric.deliverypoint.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.ServerResponse;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.LoginSignUp.SignUp.USER_MODEL;

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

        if (Utils.userData(this) != null) {
            plotData(Utils.userData(this));
        }

        permissionCheck();
        edit.setOnClickListener(v -> {

            if (Utils.userData(this) != null) {
                editData(Utils.userData(this));

                edit.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);
                profilePictureChange.setVisibility(View.VISIBLE);
                textViewName.setVisibility(View.GONE);
                textViewPhoneNO.setVisibility(View.GONE);
                textViewEmail.setVisibility(View.GONE);

                editTextName.setVisibility(View.VISIBLE);
                editTextPhoneNO.setVisibility(View.VISIBLE);
                editTextEmail.setVisibility(View.VISIBLE);


            } else {
                Utils.toast(this, "Login Required!");
            }

        });

        done.setOnClickListener(v -> {

            if (Utils.userData(this) != null) {
                doneData(Utils.userData(this));
            }

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
            if (done.getVisibility() == View.VISIBLE) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
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
            } else {
                onBackPressed();
            }
        });

        profilePictureChange.setOnClickListener(v -> {
            handlePermission();
            doThis(v);
        });


    }

    private void editData(UserModel userModel) {

        editTextName.setText(userModel.getName());
        editTextPhoneNO.setText(userModel.getPhone());
        editTextEmail.setText(userModel.getEmail());

    }

    private void doneData(UserModel userModel) {

        String name = editTextName.getText().toString();
        String phNo = editTextPhoneNO.getText().toString();
        String email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Utils.toast(this, "Enter Name");
            return;
        }
        if (TextUtils.isEmpty(phNo)) {
            Utils.toast(this, "Enter Phone No ");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Utils.toast(this, "Enter Email");
            return;
        }


        userModel.setEmail(email);
        userModel.setPhone(phNo);
        userModel.setName(name);

        if (profilePicture.getDrawable() != null) {
            BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String s = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 60);

            if (!TextUtils.isEmpty(s)) {
                userModel.setProfile(s);
                // System.out.println(s);
            }


        }

        hello();

    }

    private void plotData(UserModel userData) {
        textViewName.setText(userData.getName());
        textViewPhoneNO.setText(userData.getPhone());
        textViewEmail.setText(userData.getEmail());
        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        if (!TextUtils.isEmpty(userData.getProfile())) {
            Glide.with(this)
                    .load(userData.getProfile())
                    .apply(reqOpt)
                    .into(profilePicture);
        }


    }

    //TODO "Remaining Code here"

    private void init() {
        edit = findViewById(R.id.Edit);
        done = findViewById(R.id.Done);
        profilePictureChange = findViewById(R.id.profilePictureChange);
        profilePicture = findViewById(R.id.profilePicture);
        textViewName = findViewById(R.id.textViewName);
        textViewPhoneNO = findViewById(R.id.textViewPhoneNO);
        textViewEmail = findViewById(R.id.textViewEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNO = findViewById(R.id.editTextPhoneNO);
        editTextEmail = findViewById(R.id.editTextEmail);
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        parentRelative = findViewById(R.id.parentRelative);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showSnackBar() {
        Snackbar.make(parentRelative, "This Application needs Camera Permission", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant Permission", v -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, SETTING_INTENT_CODE);
                }).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handlePermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showSnackBar();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
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

            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                System.out.println(result.getUri());
                filePath = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    profilePicture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        switch (requestCode) {
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
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissionCheck() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }

    public void doThis(View view) {
        CropImage.activity().start(MyAccount.this);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        String encodedImage = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void setData(UserModel userModel) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putString(USER_MODEL, json);
        System.out.println(json);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("I Came here");

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.sendImage(userModel.getProfile()).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NotNull Call<ServerResponse> call, @NotNull Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().getResponse());
                    Toast.makeText(MyAccount.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ServerResponse> call, @NotNull Throwable t) {
                System.out.println(t);
                t.printStackTrace();

            }
        });
    }

    private void hello() {

        System.out.println(filePath);
        File file = new File(filePath.getPath());

//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("avatar", file);

       MultipartBody.Part body1 = prepareFilePart("avatar", filePath);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("I Came here");

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.postAvatar(body1).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().getResponse().toString());
                    RequestOptions reqOpt = RequestOptions
                            .fitCenterTransform()
                            .dontAnimate().dontTransform()
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                            .priority(Priority.IMMEDIATE)
                            .encodeFormat(Bitmap.CompressFormat.PNG)
                            .format(DecodeFormat.DEFAULT);
                    Glide.with(MyAccount.this)
                            .load("https://storage.googleapis.com/delivery-point/1613653102861.jpg")
                            .apply(reqOpt)
                            .into(profilePicture);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        null,
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}