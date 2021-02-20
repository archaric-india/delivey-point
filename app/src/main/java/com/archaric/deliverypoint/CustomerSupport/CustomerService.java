package com.archaric.deliverypoint.CustomerSupport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;

public class CustomerService extends AppCompatActivity {

    LinearLayout backToHomePageOnTitle;
    ScrollView cusSupData;
    LinearLayout noDataFoundLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        init();

        backToHomePageOnTitle.setOnClickListener(v -> onBackPressed());
        if (!Utils.isNetworkOnline(this)) {
            cusSupData.setVisibility(View.GONE);
            noDataFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init(){
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        cusSupData = findViewById(R.id.cusSupData);
        noDataFoundLayout = findViewById(R.id.noDataFoundLayout);
    }


}