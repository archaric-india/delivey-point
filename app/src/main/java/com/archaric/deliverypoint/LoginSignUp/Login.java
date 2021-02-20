package com.archaric.deliverypoint.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.EndPoint;
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

public class Login extends AppCompatActivity {

    TextView forgotPassword, createAccount, login;
    EditText emailAddress, password;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.forgotPassword);

        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,ForgotPassword.class));
        });

        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,SignUp.class));
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private void validateData(){
        String email = emailAddress.getText().toString();
        String pass = password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Utils.toast(this,"Enter Email");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Utils.toast(this,"Enter Password");
            return;
        }
        letsLogin();
    }

    private void letsLogin(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getLoginUser(emailAddress.getText().toString(),password.getText().toString()).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    if (rememberMe.isChecked()) {
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();
                        UserModel userModel = response.body();
                        String json = gson.toJson(userModel);
                        editor.putString(USER_MODEL, json);
                        System.out.println("UUUU "+response.body().toString());
                        editor.commit();
                        System.out.println(json);
                        System.out.println(userModel.getName());
                        SharedPreferences.Editor editor2 = sharedPrefs.edit();
                        editor2.putString("Auth", "True");
                        editor.commit();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();
                        UserModel userModel = response.body();
                        String json = gson.toJson(userModel);
                        editor.putString(USER_MODEL, json);
                        System.out.println("UUUU "+response.body().toString());
                        editor.commit();
                        System.out.println(json);
                        System.out.println(userModel.getName());




                        SharedPreferences.Editor editor2 = sharedPrefs.edit();
                        editor2.putString("Auth", "False");
                        editor.commit();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}