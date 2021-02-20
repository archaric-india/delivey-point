package com.archaric.deliverypoint.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.archaric.deliverypoint.R;

public class ForgotPassword extends AppCompatActivity {

    LinearLayout backToHomePageOnTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        backToHomePageOnTitle.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}