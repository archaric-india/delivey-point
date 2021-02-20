package com.archaric.deliverypoint.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AllCategories extends Fragment {

    RecyclerView recyclerView;
    AllCategoriesAdapter allCategoriesAdapter;
    ShimmerFrameLayout shimmer_view_container;

    public AllCategories() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_categories,container,false);



        recyclerView = view.findViewById(R.id.rec);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        allCategoriesAdapter = new AllCategoriesAdapter();








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
        endPoint.getAllCategories().enqueue(new Callback<List<AllCategoriesModel>>() {
            @Override
            public void onResponse(Call<List<AllCategoriesModel>> call, Response<List<AllCategoriesModel>> response) {
                ArrayList<AllCategoriesModel> arrayList = ( ArrayList<AllCategoriesModel>) response.body();
                if (arrayList != null) {
                    shimmer_view_container.stopShimmer();
                    shimmer_view_container.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    allCategoriesAdapter.setAllCategoriesModels(arrayList);
                    recyclerView.setAdapter(allCategoriesAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<AllCategoriesModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}