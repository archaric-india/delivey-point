package com.archaric.deliverypoint.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.archaric.deliverypoint.ChangeDrawerInterface;
import com.archaric.deliverypoint.Fragments.AllCategories;
import com.archaric.deliverypoint.Fragments.FiftyPerOffLarge;
import com.archaric.deliverypoint.Fragments.FiftyPercentOffers;
import com.archaric.deliverypoint.Fragments.MyOrders;
import com.archaric.deliverypoint.Fragments.NoInternetConnection;
import com.archaric.deliverypoint.Fragments.RestaurantsAroundYou;
import com.archaric.deliverypoint.Fragments.SpecialOffers;
import com.archaric.deliverypoint.Fragments.NewlyJoined;
import com.archaric.deliverypoint.IndividualRestaurant.Search;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class HomeFragment extends Fragment {

    TextView viewAllOrderHistory, viewAllSpecialOffers, viewAllFiftyPerOff,
            tryAgain, viewAllResAroundYou, viewAllNewlyJoined;
    ChangeDrawerInterface changeDrawerInterface;
    LinearLayout  noDataFoundLayout;
    NestedScrollView mainContentLayout;
    FrameLayout searchOnHome;
    EditText search_bar;
    View divider;
    SearchRestaurantData searchRestaurantData;
    private BroadcastReceiver collapseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean expanded = intent.getBooleanExtra("APPBAR", false);
            if(expanded){
                divider.setVisibility(View.GONE);
            }else {
                divider.setVisibility(View.VISIBLE);
            }
        }
    };
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
            viewAllNewlyJoined = root.findViewById(R.id.viewAllNewlyJoined);
            viewAllResAroundYou = root.findViewById(R.id.viewAllResAroundYou);
            tryAgain = root.findViewById(R.id.tryAgain);
            viewAllFiftyPerOff = root.findViewById(R.id.viewAllFiftyPerOff);
            viewAllOrderHistory = root.findViewById(R.id.viewAllOrderHistory);
            viewAllSpecialOffers = root.findViewById(R.id.viewAllSpecialOffers);
            mainContentLayout = root.findViewById(R.id.mainContentLayout);
            noDataFoundLayout = root.findViewById(R.id.noDataFoundLayout);
            searchOnHome = root.findViewById(R.id.searchOnHome);
            search_bar = root.findViewById(R.id.search_bar);
            divider = root.findViewById(R.id.divider);

        if (!Utils.isNetworkOnline(getActivity())) {
            noDataFoundLayout.setVisibility(View.VISIBLE);
            mainContentLayout.setVisibility(View.GONE);
        }




            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.allCategoriesContent,new AllCategories());
            transaction.commit();

            FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction1.replace(R.id.myOrders,new MyOrders());
            transaction1.commit();

            FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction2.replace(R.id.specialOffers,new SpecialOffers());
            transaction2.commit();


            FragmentTransaction transaction3 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction3.replace(R.id.fiftyPerOffers,new FiftyPercentOffers());
            transaction3.commit();

            FragmentTransaction transaction4 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction4.replace(R.id.restaurants_around_you,new RestaurantsAroundYou());
            transaction4.commit();

            FragmentTransaction transaction5 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction5.replace(R.id.newly_joined,new NewlyJoined());
            transaction5.commit();


        try {
            searchRestaurantData = (SearchRestaurantData) getActivity();

        }catch (Exception e){
            e.printStackTrace();
            searchRestaurantData = null;
        }



        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(collapseReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(collapseReceiver, new IntentFilter("APPBAR"));
    }

    SearchRestaurant searchRestaurant;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(HomeFragment.this);
                ft.attach(HomeFragment.this).commit();
            }
        });

        viewAllNewlyJoined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeDrawerInterface = (ChangeDrawerInterface) getActivity();
                } catch (ClassCastException e) {
                    changeDrawerInterface = null;
                    e.printStackTrace();
                }
                if (changeDrawerInterface != null) {
                    changeDrawerInterface.DrawerData("toNewlyJoinedLargeFragment");
                }
            }
        });

        viewAllResAroundYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeDrawerInterface = (ChangeDrawerInterface) getActivity();
                } catch (ClassCastException e) {
                    changeDrawerInterface = null;
                    e.printStackTrace();
                }
                if (changeDrawerInterface != null) {
                    changeDrawerInterface.DrawerData("toResAroundYouLargeFragment");
                }
            }
        });


        viewAllOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeDrawerInterface = (ChangeDrawerInterface) getActivity();
                } catch (ClassCastException e) {
                    changeDrawerInterface = null;
                    e.printStackTrace();
                }
                if (changeDrawerInterface != null) {
                    changeDrawerInterface.DrawerData("toOrdersHistoryFragment");
                }

            }
        });

        viewAllFiftyPerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeDrawerInterface = (ChangeDrawerInterface) getActivity();
                } catch (ClassCastException e) {
                    changeDrawerInterface = null;
                    e.printStackTrace();
                }
                if (changeDrawerInterface != null) {
                    changeDrawerInterface.DrawerData("toFiftyPerOffFragment");
                }

            }
        });

        viewAllSpecialOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeDrawerInterface = (ChangeDrawerInterface) getActivity();
                } catch (ClassCastException e) {
                    changeDrawerInterface = null;
                    e.printStackTrace();
                }
                if (changeDrawerInterface != null) {
                    changeDrawerInterface.DrawerData("toOffersFragment");
                }

            }
        });

        search_bar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    searchOnHome.setVisibility(View.VISIBLE);
                    searchRestaurant = new SearchRestaurant();
                    FragmentTransaction transaction5 = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction5.replace(R.id.searchOnHome,searchRestaurant);
                    transaction5.commit();
                }
            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (searchRestaurant != null){
                    searchOnHome.setVisibility(View.VISIBLE);
                    searchRestaurant.getSearchData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {
                    searchOnHome.setVisibility(View.GONE);
                }

            }
        });

    }
}