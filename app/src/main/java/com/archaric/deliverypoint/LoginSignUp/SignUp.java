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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.IndividualRestaurant.IndividualResCategoryModel;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Settings.SettingsContent;
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

import static com.archaric.deliverypoint.Settings.Settings.SETTINGS_CONTENT_KEY;

public class SignUp extends AppCompatActivity {

    LinearLayout backToHomePageOnTitle;
    TextView terms, conditions , signUp;
    EditText cusName, phoneNum, emailAddress, password;
    CheckBox agree;
    public static final String USER_MODEL = "UserModel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        terms = findViewById(R.id.terms);
        conditions = findViewById(R.id.conditions);
        cusName = findViewById(R.id.cusName);
        phoneNum = findViewById(R.id.phoneNum);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signUp);
        agree = findViewById(R.id.agree);
        backToHomePageOnTitle.setOnClickListener(v -> {
            onBackPressed();
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SettingsContent.class);
                intent.putExtra(SETTINGS_CONTENT_KEY,"PrivacyPolicy");
                startActivity(intent);
            }
        });

        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SettingsContent.class);
                intent.putExtra(SETTINGS_CONTENT_KEY,"TermsConditions");
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

    }

    private void validateFields(){
        String name = cusName.getText().toString();
        String num = phoneNum.getText().toString();
        String email = emailAddress.getText().toString();
        String pass = password.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Utils.toast(this,"Enter Name");
            return;
        }
        if (TextUtils.isEmpty(num)) {
            Utils.toast(this,"Enter Phone Number");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Utils.toast(this,"Enter Email");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Utils.toast(this,"Enter Password");
            return;
        }

        if (!agree.isChecked()) {
            Utils.toast(this,"Agree Policy's");
            return;
        }

        signUpData();

    }

    private void signUpData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.setSignUp(cusName.getText().toString(),phoneNum.getText().toString(),
                emailAddress.getText().toString(),password.getText().toString())
                .enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(SignUp.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Utils.toast(SignUp.this,"SignUp Successful");
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(SignUp.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}