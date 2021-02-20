package com.archaric.deliverypoint.Settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;


public class ChangeLanguage extends Fragment {


    LinearLayout backToHomePageOnTitle;
    RadioButton englishLang, arabicLang;
    RadioGroup langRadioGroup;
    TextView save;

    boolean english = true;

    public ChangeLanguage() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_language, container, false);

        save  = view.findViewById(R.id.save);
        backToHomePageOnTitle = view.findViewById(R.id.backToHomePageOnTitle);
        englishLang = view.findViewById(R.id.englishLang);
        arabicLang = view.findViewById(R.id.arabicLang);
        langRadioGroup = view.findViewById(R.id.langRadioGroup);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backToHomePageOnTitle.setOnClickListener(v -> getActivity().onBackPressed());

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = sharedPrefs.getString("Lang", "en");
        System.out.println(json + "HERE");
        if (json.equals("en")){
            englishLang.setChecked(true);
            setAppLocale("en");
        }else {
            arabicLang.setChecked(true);
            setAppLocale("ar");
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (english){
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("Lang", "en");
                    editor.commit();
                    setAppLocale("en");
                    restartActivity();
                }else {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("Lang", "ar");
                    editor.commit();
                    setAppLocale("ar");
                    restartActivity();
                }
            }
        });



        langRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)view.findViewById(checkedId);
                Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();
                if (checkedId == R.id.englishLang){
                       english = true;
                }else if (checkedId == R.id.arabicLang){
                       english = false;
                }

            }
        });


    }

    public void setAppLocale(String localeKey){
        Resources resources =getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            conf.setLocale(new Locale(localeKey.toLowerCase()));
        }else {
            conf.locale = new Locale(localeKey.toLowerCase());
        }
        resources.updateConfiguration(conf, dm);

    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}