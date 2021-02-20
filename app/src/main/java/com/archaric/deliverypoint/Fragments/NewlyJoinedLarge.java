package com.archaric.deliverypoint.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Mapset.ZONE_ID;


public class NewlyJoinedLarge extends Fragment {

    RecyclerView resNewlyJoinedLargeRec;
    LinearLayout noInternetFoundLayout;
    ShimmerFrameLayout shimmer_layout;
    TextView tryAgain;
    RestaurantsAroundYouLargeAdapter restaurantsAroundYouLargeAdapter;

    public NewlyJoinedLarge() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newly_joined_large, container, false);
                tryAgain  = view.findViewById(R.id.tryAgain);
        resNewlyJoinedLargeRec  = view.findViewById(R.id.resNewlyJoinedLargeRec);
        noInternetFoundLayout  =view.findViewById(R.id.noInternetFoundLayout);
        shimmer_layout  =view.findViewById(R.id.shimmer_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        resNewlyJoinedLargeRec.setLayoutManager(layoutManager);
        restaurantsAroundYouLargeAdapter = new RestaurantsAroundYouLargeAdapter();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getData();

    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EndPoint endPoint = retrofit.create(EndPoint.class);
        String zoneId = Utils.getStoredData(getActivity(),ZONE_ID);
        System.out.println(zoneId + "Zone Id at Newly Joined");
        if (!TextUtils.isEmpty(zoneId)) {
            endPoint.getNewlyJoined(zoneId).enqueue(new Callback<List<FiftyPercentOfferModel>>() {
                @Override
                public void onResponse(Call<List<FiftyPercentOfferModel>> call, Response<List<FiftyPercentOfferModel>> response) {
                    ArrayList<FiftyPercentOfferModel> offerModels = (ArrayList<FiftyPercentOfferModel>) response.body();
                    if (offerModels != null) {
                        shimmer_layout.stopShimmer();
                        shimmer_layout.setVisibility(View.GONE);
                        resNewlyJoinedLargeRec.setVisibility(View.VISIBLE);
                        restaurantsAroundYouLargeAdapter.setModels(offerModels);
                        resNewlyJoinedLargeRec.setAdapter(restaurantsAroundYouLargeAdapter);
                        restaurantsAroundYouLargeAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<List<FiftyPercentOfferModel>> call, Throwable t) {

                }
            });
        }



    }
}