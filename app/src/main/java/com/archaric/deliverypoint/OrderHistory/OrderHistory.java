package com.archaric.deliverypoint.OrderHistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.ChangeDrawerInterface;
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


public class OrderHistory extends Fragment {

    LinearLayout noDataFoundLayout,noInternetFoundLayout;
    RecyclerView orderHistoryRec;
    ShimmerFrameLayout shimmerFrameLayout;
    OrderDetailsAdapter orderDetailsAdapter;
    ChangeDrawerInterface changeDrawerInterface;
    TextView backToHomePageFromOrderHistory;


    public OrderHistory() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        backToHomePageFromOrderHistory = view.findViewById(R.id.backToHomePageFromOrderHistory);
        noDataFoundLayout = view.findViewById(R.id.noDataFoundLayout);
        noInternetFoundLayout = view.findViewById(R.id.noInternetFoundLayout);
        orderHistoryRec = view.findViewById(R.id.orderHistoryRec);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        orderHistoryRec.setLayoutManager(layoutManager);
        orderDetailsAdapter = new OrderDetailsAdapter();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!Utils.isNetworkOnline(getActivity())) {
            noDataFoundLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            noInternetFoundLayout.setVisibility(View.VISIBLE);
        }else {
            noInternetFoundLayout.setVisibility(View.GONE);
        }

        backToHomePageFromOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeDrawerInterface = (ChangeDrawerInterface) getActivity();
                } catch (ClassCastException e) {
                    changeDrawerInterface = null;
                    e.printStackTrace();
                }
                if (changeDrawerInterface != null) {
                    changeDrawerInterface.DrawerData("toHomeFragment");
                }
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

        if (Utils.userData(getActivity()) != null) {
            endPoint.getOrderDetails(Utils.userData(getActivity()).getId()).enqueue(new Callback<List<OrdersModel>>() {
                @Override
                public void onResponse(Call<List<OrdersModel>> call, Response<List<OrdersModel>> response) {

                    ArrayList<OrdersModel> orderDetailsModels = (ArrayList<OrdersModel>) response.body();
                    if (response.isSuccessful()){
                        if (orderDetailsModels.size() != 0) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            noDataFoundLayout.setVisibility(View.GONE);
                            orderHistoryRec.setVisibility(View.VISIBLE);
                            orderDetailsAdapter.setDetailsModels(orderDetailsModels);
                            orderHistoryRec.setAdapter(orderDetailsAdapter);
                            orderDetailsAdapter.notifyDataSetChanged();
                        }else {
                            noDataFoundLayout.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            orderHistoryRec.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrdersModel>> call, Throwable t) {

                }
            });
        }else {
            noDataFoundLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            orderHistoryRec.setVisibility(View.GONE);
        }




    }
}