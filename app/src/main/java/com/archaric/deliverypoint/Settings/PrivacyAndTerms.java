package com.archaric.deliverypoint.Settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.R;


public class PrivacyAndTerms extends Fragment {


    TextView resNameTop;
    LinearLayout backToHomePageOnTitle;

    String str;

    public PrivacyAndTerms(String s) {
        if (!TextUtils.isEmpty(s)) {
            str = s;
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_and_terms, container, false);
        resNameTop = view.findViewById(R.id.resNameTop);
        backToHomePageOnTitle = view.findViewById(R.id.backToHomePageOnTitle);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!TextUtils.isEmpty(str)) {
            resNameTop.setText(str);
        }

        backToHomePageOnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


    }
}