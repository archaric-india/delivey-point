package com.archaric.deliverypoint;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.archaric.deliverypoint.LoginSignUp.SignUp.USER_MODEL;

public class Utils {

    public static boolean isNetworkOnline(Context con){
        boolean status = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);

                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    status = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

    public static  void toast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static UserModel userData(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(USER_MODEL,"");
        Type type = new TypeToken<UserModel>() {}.getType();
        UserModel userModel = gson.fromJson(json, type);
        return userModel;
    }

    public static void storeData(Context context, String data, String key){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //Gson gson = new Gson();

        //String json = sharedPrefs.getString("TAG", "[]");
        //Type type = new TypeToken<List<Items>>() {}.getType();
        //itemsArrayList = gson.fromJson(json, type);

        //itemsArrayList.add(item);
        //json = gson.toJson(itemsArrayList);
        editor.putString(key, data);
        editor.commit();
    }

    public static String getStoredData(Context context, String key){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        return sharedPrefs.getString(key, "");
        //editor.commit();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        //imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

}
