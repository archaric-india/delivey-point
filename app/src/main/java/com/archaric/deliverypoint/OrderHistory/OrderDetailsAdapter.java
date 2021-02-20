package com.archaric.deliverypoint.OrderHistory;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.Fragments.SetPlaceOrderData;
import com.archaric.deliverypoint.R;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    public static final String DELIVERY_DATA = "DeliveryData";
    ArrayList<OrdersModel> detailsModels;
    public static final String DELIVERED_KEY = "deliveredKey";

    public void setDetailsModels(ArrayList<OrdersModel> detailsModels) {
        this.detailsModels = detailsModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.orderNumber.setText(detailsModels.get(position).getId());
        holder.orderStatus.setText(detailsModels.get(position).getStatus());
        holder.orderTotal.setText("KWD "+String.valueOf(detailsModels.get(position).getTotal()));

        if (detailsModels.get(position).getStatus().equals("Delivered")) {
            holder.expTime.setVisibility(View.GONE);
        }else if(detailsModels.get(position).getStatus().equals("Out For Delivery")) {
            holder.expTime.setVisibility(View.VISIBLE);
            holder.expTime.setText("Delivery in "+detailsModels.get(position).getExptime()+" minutes with live tracking");
        }else {
            holder.expTime.setVisibility(View.GONE);
        }

        holder.orderCheckLayout.setOnClickListener(new View.OnClickListener() {
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
        TextView orderNumber,orderStatus,orderTotal, expTime;
        LinearLayout orderCheckLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            orderNumber = itemView.findViewById(R.id.orderNumber);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            expTime = itemView.findViewById(R.id.expTime);

            orderCheckLayout = itemView.findViewById(R.id.orderCheckLayout);



        }
    }
}
