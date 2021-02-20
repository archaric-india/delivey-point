package com.archaric.deliverypoint.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.archaric.deliverypoint.Fragments.AddressModel;
import com.archaric.deliverypoint.Fragments.LocationPicker;
import com.archaric.deliverypoint.Fragments.NoInternetConnection;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;


import java.util.ArrayList;

import static com.archaric.deliverypoint.Settings.Settings.SETTINGS_CONTENT_KEY;

public class SettingsContent extends AppCompatActivity implements AddressDataInterface {

    Bundle extras;
    Uri filePath;

    public Uri getFilePath() {
        return filePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_content);

        extras = getIntent().getExtras();
        if(extras != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            String val = extras.getString(SETTINGS_CONTENT_KEY);
            switch (val){

                case "MyAddresses":
                    transaction.replace(R.id.settingsChanger,new MyAddresses());
                    transaction.commit();
                    break;
                case "ChangePassword":
                    transaction.replace(R.id.settingsChanger,new ChangePassword());
                    transaction.commit();
                    break;
                case "ChangeLanguage":
                    transaction.replace(R.id.settingsChanger,new ChangeLanguage());
                    transaction.commit();
                    break;
                case "MyNotifications":
                    transaction.replace(R.id.settingsChanger,new MyNotification());
                    transaction.commit();
                    break;
                case "PrivacyPolicy":
                    transaction.replace(R.id.settingsChanger,new PrivacyAndTerms("Privacy Policy"));
                    transaction.commit();
                    break;
                case "TermsConditions":
                    transaction.replace(R.id.settingsChanger,new PrivacyAndTerms("Terms & Conditions"));
                    transaction.commit();
                    break;

            }
        }

        if (!Utils.isNetworkOnline(this)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.settingsChanger,new NoInternetConnection());
            transaction.commit();
        }
    }

    @Override
    public void AddressIntentData(String s) {

        if (!TextUtils.isEmpty(s)){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (s.equals("Edit Address")) {
                Intent intent = new Intent(SettingsContent.this,AddNewAddress.class);
                intent.putExtra("AddAddress","Edit Address");
                startActivity(intent);
//                transaction.replace(R.id.settingsChanger,new AddAndEditAddresses(s));
//                transaction.commit();
            }

            if (s.equals("MyAddresses")) {
                transaction.replace(R.id.settingsChanger,new MyAddresses());
                transaction.commit();
            }

            if (s.equals("Add a New Address")) {
                Intent intent = new Intent(SettingsContent.this,AddNewAddress.class);
                intent.putExtra("AddAddress","Add a New Address");
                startActivity(intent);
            }

            if (s.equals("LocationPicker")) {
                transaction.replace(R.id.settingsChanger,new LocationPicker());
                transaction.commit();
            }

        }

    }

}