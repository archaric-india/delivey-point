package com.archaric.deliverypoint.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    public static final String NOTIFICATION_CHECK = "NotificationCheck";
    LinearLayout backToHomePageOnTitle, settingsContent;
    FrameLayout settingsChanger;
    RelativeLayout myAccountLayout, myAddressesLayout, changePasswordLayout, changeLanguageLayout, myNotifications, privacyPolicy, termsAndConditions;
    Intent intent;
    TextView currentLang;
    SwitchMaterial switchNotification;
    public static final String SETTINGS_CONTENT_KEY = "SettingsContentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_settings);
      init();


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = sharedPrefs.getString("Lang", "en");
        System.out.println(json + "HERE");
        if (json.equals("en")){
            currentLang.setText("English");
        }else {
            currentLang.setText("Arabic");
        }

        String notify = Utils.getStoredData(Settings.this,NOTIFICATION_CHECK);
        if(notify.equals("Enable")){
            switchNotification.setChecked(true);
        }if (notify.equals("Disable")){
            switchNotification.setChecked(false);
        }


        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Utils.storeData(Settings.this,"Enable",NOTIFICATION_CHECK);
                    Utils.toast(Settings.this,"Notification turned On!");
                }else {
                    Utils.storeData(Settings.this,"Disable",NOTIFICATION_CHECK);
                    Utils.toast(Settings.this,"Notification turned Off!");
                }
            }
        });


    }

    private void init(){
        switchNotification  = findViewById(R.id.switchNotification);
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        settingsContent = findViewById(R.id.settingsContent);
        settingsChanger = findViewById(R.id.settingsChanger);
        myAccountLayout = findViewById(R.id.myAccountLayout);
        myAddressesLayout = findViewById(R.id.myAddressesLayout);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        changeLanguageLayout = findViewById(R.id.changeLanguageLayout);
        myNotifications = findViewById(R.id.myNotifications);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        termsAndConditions = findViewById(R.id.termsAndConditions);
        currentLang = findViewById(R.id.currentLang);

        backToHomePageOnTitle.setOnClickListener(this);
        myAccountLayout.setOnClickListener(this);
        myAddressesLayout.setOnClickListener(this);
        changePasswordLayout.setOnClickListener(this);
        changeLanguageLayout.setOnClickListener(this);
        myNotifications.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        intent = new Intent(this,SettingsContent.class);
        switch (v.getId()){
            case R.id.myAccountLayout :
                startActivity(new Intent(Settings.this, MyAccount.class));
                break;
            case R.id.myAddressesLayout :
                intent.putExtra(SETTINGS_CONTENT_KEY,"MyAddresses");
                startActivity(intent);
                break;
            case R.id.changePasswordLayout :
                intent.putExtra(SETTINGS_CONTENT_KEY,"ChangePassword");
                startActivity(intent);
                break;
            case R.id.changeLanguageLayout :
                intent.putExtra(SETTINGS_CONTENT_KEY,"ChangeLanguage");
                startActivity(intent);
                break;
            case R.id.myNotifications :
                intent.putExtra(SETTINGS_CONTENT_KEY,"MyNotifications");
                startActivity(intent);
                break;
            case R.id.privacyPolicy :
                intent.putExtra(SETTINGS_CONTENT_KEY,"PrivacyPolicy");
                startActivity(intent);
                break;
            case R.id.termsAndConditions :
                intent.putExtra(SETTINGS_CONTENT_KEY,"TermsConditions");
                startActivity(intent);
                break;

            case R.id.backToHomePageOnTitle :
                onBackPressed();
                break;
            default:
                break;
        }

    }
}