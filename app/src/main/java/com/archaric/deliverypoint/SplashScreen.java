package com.archaric.deliverypoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.archaric.deliverypoint.LoginSignUp.ForgotPassword;
import com.archaric.deliverypoint.LoginSignUp.Login;
import com.archaric.deliverypoint.LoginSignUp.SignUp;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    int SPLASH_TIME = 1000; //This is 1 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = sharedPrefs.getString("Lang", "en");
        System.out.println(json + "HERE");
        if (json.equals("en")){
            setAppLocale("en");
        }else {
            setAppLocale("ar");
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    startActivity(new Intent(SplashScreen.this, Mapset.class));
                    finish();
                    //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                } catch (Exception ex) {
                    // Here we are logging the exception to see why it happened.
                    Log.e("my app", ex.toString());
                }
            }
        }, SPLASH_TIME);

    }

    public void setAppLocale(String localeKey){
        Resources resources = getBaseContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            conf.setLocale(new Locale(localeKey.toLowerCase()));
        }else {
            conf.locale = new Locale(localeKey.toLowerCase());
        }
        resources.updateConfiguration(conf, dm);

    }
}