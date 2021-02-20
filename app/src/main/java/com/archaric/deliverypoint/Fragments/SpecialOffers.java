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


public class SpecialOffers extends Fragment {

    SpecialOffersAdapter specialOffersAdapter;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;

    public SpecialOffers() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offers,container,false);



        recyclerView = view.findViewById(R.id.rec);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        specialOffersAdapter = new SpecialOffersAdapter();




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
        System.out.println(zoneId + "Zone Id at Special Offers");
        if (!TextUtils.isEmpty(zoneId)){

            endPoint.getSpecialOffers(zoneId).enqueue(new Callback<List<AllCategoriesModel>>() {
                @Override
                public void onResponse(Call<List<AllCategoriesModel>> call, Response<List<AllCategoriesModel>> response) {
                    ArrayList<AllCategoriesModel> allCategoriesModels = (ArrayList<AllCategoriesModel>)  response.body();
                    if (allCategoriesModels != null) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        specialOffersAdapter.setModelArrayList(allCategoriesModels);
                        recyclerView.setAdapter(specialOffersAdapter);

                    }
                }

                @Override
                public void onFailure(Call<List<AllCategoriesModel>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}