package com.archaric.deliverypoint.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.OrderHistory.OrderDetailsModel;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
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


public class MyOrders extends Fragment {

    RecyclerView myOrdersRec;
    MyOrdersAdapter orderHistoryOnHomeAdapter;
    CardView noOrderHistoryFound;
    ShimmerFrameLayout shimmer_layout;

    public MyOrders() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrdersRec = view.findViewById(R.id.myOrdersRec);
        noOrderHistoryFound = view.findViewById(R.id.noOrderHistoryFound);
        shimmer_layout = view.findViewById(R.id.shimmer_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        myOrdersRec.setLayoutManager(layoutManager);
        orderHistoryOnHomeAdapter = new MyOrdersAdapter();


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

        EndPoint endPoint = retrofit.create(EndPoint.class);

        if (Utils.userData(getActivity()) != null) {
            System.out.println(Utils.userData(getActivity()).getId());
            endPoint.getOrderDetails(Utils.userData(getActivity()).getId()).enqueue(new Callback<List<OrdersModel>>() {
                @Override
                public void onResponse(Call<List<OrdersModel>> call, Response<List<OrdersModel>> response) {
                    ArrayList<OrdersModel> orderDetailsModels = (ArrayList<OrdersModel>) response.body();
                    if (response.isSuccessful()) {
                        if (orderDetailsModels.size() != 0) {
                            shimmer_layout.stopShimmer();
                            shimmer_layout.setVisibility(View.GONE);
                            noOrderHistoryFound.setVisibility(View.GONE);
                            myOrdersRec.setVisibility(View.VISIBLE);
                            orderHistoryOnHomeAdapter.setDetailsModels(orderDetailsModels);
                            myOrdersRec.setAdapter(orderHistoryOnHomeAdapter);
                            orderHistoryOnHomeAdapter.notifyDataSetChanged();
                        } else {
                            noOrderHistoryFound.setVisibility(View.VISIBLE);
                            shimmer_layout.stopShimmer();
                            shimmer_layout.setVisibility(View.GONE);
                            myOrdersRec.setVisibility(View.GONE);
                        }
                    }


                }

                @Override
                public void onFailure(Call<List<OrdersModel>> call, Throwable t) {

                }


            });
        }else {
            noOrderHistoryFound.setVisibility(View.VISIBLE);
            shimmer_layout.stopShimmer();
            shimmer_layout.setVisibility(View.GONE);
            myOrdersRec.setVisibility(View.GONE);
        }
    }
}