package com.archaric.deliverypoint.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

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


public class FiftyPercentOffers extends Fragment {


    FiftyPercentOffersAdapter fiftyPercentOffersAdapter;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmer_view_container;

    public FiftyPercentOffers() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fifty_percent_offers,container,false);



        recyclerView = view.findViewById(R.id.rec);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        fiftyPercentOffersAdapter = new FiftyPercentOffersAdapter();




        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       getData();
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String zoneId = Utils.getStoredData(getActivity(),ZONE_ID);
        System.out.println(zoneId + "Zone Id at 50% Offers");

        EndPoint endPoint = retrofit.create(EndPoint.class);

        if (!TextUtils.isEmpty(zoneId)){

            endPoint.getFiftyOffers(zoneId).enqueue(new Callback<List<FiftyPercentOfferModel>>() {
                @Override
                public void onResponse(Call<List<FiftyPercentOfferModel>> call, Response<List<FiftyPercentOfferModel>> response) {
                    ArrayList<FiftyPercentOfferModel> fiftyPercentOfferModels = ( ArrayList<FiftyPercentOfferModel>) response.body();
                    if (fiftyPercentOfferModels != null) {
                        shimmer_view_container.stopShimmer();
                        shimmer_view_container.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        fiftyPercentOffersAdapter.setFiftyPercentOfferModels(fiftyPercentOfferModels);
                        recyclerView.setAdapter(fiftyPercentOffersAdapter);
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