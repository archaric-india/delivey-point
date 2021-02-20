package com.archaric.deliverypoint.Fragments;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.OrderHistory.OrderDetailsModel;
import com.archaric.deliverypoint.OrderHistory.MyOrders;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.R;

import java.util.ArrayList;

import static com.archaric.deliverypoint.OrderHistory.OrderDetailsAdapter.DELIVERED_KEY;
import static com.archaric.deliverypoint.OrderHistory.OrderDetailsAdapter.DELIVERY_DATA;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    ArrayList<OrdersModel> detailsModels;

    public void setDetailsModels(ArrayList<OrdersModel> detailsModels) {
        this.detailsModels = detailsModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.orderStatus.setText(detailsModels.get(position).getStatus());
        holder.orderTotal.setText("KD "+String.valueOf(detailsModels.get(position).getTotal()));
       // holder.expTime.setText("Delivery in "+detailsModels.get(position).getExptime()+" minutes with live tracking");

        if (detailsModels.get(position).getStatus().equals("Delivered")) {
            holder.expTime.setVisibility(View.GONE);
        }else if(detailsModels.get(position).getStatus().toString().trim().equals("Out For Delivery")) {
            holder.expTime.setVisibility(View.VISIBLE);
            holder.expTime.setText("Delivery in "+detailsModels.get(position).getExptime()+" minutes with live tracking");
        }else {
            holder.expTime.setVisibility(View.GONE);
        }

        holder.orderCheckCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.expTime.getContext(), MyOrders.class);
                SetPlaceOrderData.setOrdersModel(detailsModels.get(position));

                if (detailsModels.get(position).getStatus().equals("Delivered")) {
                    intent.putExtra(DELIVERED_KEY,"Delivered");
                    holder.orderTotal.getContext().startActivity(intent);
                }else if(detailsModels.get(position).getStatus().equals("Out For Delivery")) {
                    intent.putExtra(DELIVERED_KEY,"OrderTracking");
                    holder.orderTotal.getContext().startActivity(intent);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return detailsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderStatus,orderTotal, expTime;
        CardView orderCheckCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderCheckCardView  = itemView.findViewById(R.id.orderCheckCardView);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            expTime = itemView.findViewById(R.id.expTime);

        }
    }
}
