package com.archaric.deliverypoint.OrderHistory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.archaric.deliverypoint.R;

import static com.archaric.deliverypoint.OrderHistory.OrderDetailsAdapter.DELIVERED_KEY;

public class OrderDelivered extends Fragment {

    LinearLayout backToHomePageOnTitle, writeAReview;

    public OrderDelivered() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_delivered, container, false);

        backToHomePageOnTitle = view.findViewById(R.id.backToHomePageOnTitle);
        writeAReview = view.findViewById(R.id.writeAReview);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backToHomePageOnTitle.setOnClickListener(v -> getActivity().onBackPressed());
        writeAReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOrders.class);
                intent.putExtra(DELIVERED_KEY,"WriteAReview");
                getActivity().startActivity(intent);
            }
        });
    }
}