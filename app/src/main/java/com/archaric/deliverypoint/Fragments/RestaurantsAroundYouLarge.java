package com.archaric.deliverypoint.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.archaric.deliverypoint.ui.home.HomeFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Mapset.ZONE_ID;


public class RestaurantsAroundYouLarge extends Fragment {

    RecyclerView resAroundYouLargeRec;
    LinearLayout noInternetFoundLayout;
    ShimmerFrameLayout shimmer_layout;
    TextView tryAgain;
    RestaurantsAroundYouLargeAdapter restaurantsAroundYouLargeAdapter;

    public RestaurantsAroundYouLarge() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants_around_you_large, container, false);

        tryAgain  = view.findViewById(R.id.tryAgain);
        resAroundYouLargeRec  =view.findViewById(R.id.resAroundYouLargeRec);
        noInternetFoundLayout  =view.findViewById(R.id.noInternetFoundLayout);
        shimmer_layout  =view.findViewById(R.id.shimmer_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        resAroundYouLargeRec.setLayoutManager(layoutManager);
        restaurantsAroundYouLargeAdapter = new RestaurantsAroundYouLargeAdapter();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Utils.isNetworkOnline(getActivity())){
            noInternetFoundLayout.setVisibility(View.VISIBLE);
        }

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(RestaurantsAroundYouLarge.this);
                ft.attach(RestaurantsAroundYouLarge.this).commit();
            }
        });

       getData();

    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EndPoint endPoint = retrofit.create(EndPoint.class);
        String zoneId = Utils.getStoredData(getActivity(),ZONE_ID);
        System.out.println(zoneId + "Zone Id at Res Around You Large");


        if (!TextUtils.isEmpty(zoneId)) {
            endPoint.getResAroundYou(13.0849,80.4756, zoneId).enqueue(new Callback<List<FiftyPercentOfferModel>>() {
                @Override
                public void onResponse(Call<List<FiftyPercentOfferModel>> call, Response<List<FiftyPercentOfferModel>> response) {
                    ArrayList<FiftyPercentOfferModel> fiftyPercentOfferModels = ( ArrayList<FiftyPercentOfferModel>) response.body();
                    if (fiftyPercentOfferModels != null) {
                        shimmer_layout.stopShimmer();
                        shimmer_layout.setVisibility(View.GONE);
                        resAroundYouLargeRec.setVisibility(View.VISIBLE);
                        restaurantsAroundYouLargeAdapter.setModels(fiftyPercentOfferModels);
                        resAroundYouLargeRec.setAdapter(restaurantsAroundYouLargeAdapter);
                        restaurantsAroundYouLargeAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onFailure(Call<List<FiftyPercentOfferModel>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }
}