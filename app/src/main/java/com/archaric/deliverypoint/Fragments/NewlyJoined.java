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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.Fragments.FiftyPercentOffersAdapter;
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


public class NewlyJoined extends Fragment {


    ResAroundYouANDNewlyAdapter resAroundYouANDNewlyAdapter;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;

    public NewlyJoined() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newly_joined, container, false);

        recyclerView = view.findViewById(R.id.rec);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        resAroundYouANDNewlyAdapter = new ResAroundYouANDNewlyAdapter();




        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                        //System.out.println(offerModels.get(0).getCategory());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        resAroundYouANDNewlyAdapter.setModels(offerModels);
                        recyclerView.setAdapter(resAroundYouANDNewlyAdapter);
                        resAroundYouANDNewlyAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<List<FiftyPercentOfferModel>> call, Throwable t) {

                }
            });
        }






    }
}