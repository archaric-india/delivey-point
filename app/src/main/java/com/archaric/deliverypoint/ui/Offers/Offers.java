package com.archaric.deliverypoint.ui.Offers;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.archaric.deliverypoint.Fragments.AllCategoriesModel;
import com.archaric.deliverypoint.Fragments.SpecialOffersAdapter;
import com.archaric.deliverypoint.Fragments.SpecialOffersAdapterLargeView;
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

public class Offers extends Fragment {

    RecyclerView offersRec;
    ShimmerFrameLayout shimmerFrameLayout;
    SpecialOffersAdapterLargeView specialOffersAdapter;
    LinearLayout noInternetFoundLayout;
    TextView tryAgain;

    public Offers() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        tryAgain  = view.findViewById(R.id.tryAgain);
        offersRec = view.findViewById(R.id.offersRec);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        noInternetFoundLayout = view.findViewById(R.id.noInternetFoundLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        offersRec.setLayoutManager(layoutManager);
        specialOffersAdapter = new SpecialOffersAdapterLargeView();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Utils.isNetworkOnline(getActivity())) {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            noInternetFoundLayout.setVisibility(View.VISIBLE);
        }else {
            noInternetFoundLayout.setVisibility(View.GONE);
        }

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(Offers.this);
                ft.attach(Offers.this).commit();
            }
        });

        getData();
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EndPoint endPoint = retrofit.create(EndPoint.class);

        String zoneId = Utils.getStoredData(getActivity(),ZONE_ID);
        System.out.println(zoneId + "Zone Id at Offers Large View");
        if (!TextUtils.isEmpty(zoneId)){

            endPoint.getSpecialOffers(zoneId).enqueue(new Callback<List<AllCategoriesModel>>() {
                @Override
                public void onResponse(Call<List<AllCategoriesModel>> call, Response<List<AllCategoriesModel>> response) {
                    ArrayList<AllCategoriesModel> allCategoriesModels = (ArrayList<AllCategoriesModel>)  response.body();
                    if (allCategoriesModels.size() != 0) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        offersRec.setVisibility(View.VISIBLE);
                        specialOffersAdapter.setModelArrayList(allCategoriesModels);
                        offersRec.setAdapter(specialOffersAdapter);
                        specialOffersAdapter.notifyDataSetChanged();

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