package com.archaric.deliverypoint.OrderHistory;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.Fragments.AddressModel;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChangeAddressAtCartAdapter extends RecyclerView.Adapter<ChangeAddressAtCartAdapter.ViewHolder>  {

        ArrayList<OrdersModel> ordersModels;
        private int previousPosition = 0;
        SendAddressPosition sendAddressPosition;




    public void setOrdersModels(ArrayList<OrdersModel> ordersModels, Context context) {
        this.ordersModels = ordersModels;
        String json = Utils.getStoredData(context, "ADDRESS");
        if(!json.isEmpty()){
            Gson gson = new Gson();
            Type type = new TypeToken<OrdersModel>() {}.getType();
            OrdersModel address = gson.fromJson(json, type);
            for (OrdersModel o: this.ordersModels) {
                if(o.equals(address)){
                    //int idx = this.ordersModels.indexOf(o);
                    this.ordersModels.remove(o);
                    this.ordersModels.add(0, o);
                    //previousPosition = this.ordersModels.indexOf(o);
                    break;
                }
            }
        }


        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.cusName.setText(ordersModels.get(position).getCustomername());
        holder.deliveryTo.setText(ordersModels.get(position).getDeliveryto());
        holder.roomNoAndBlockAndCity.setText(ordersModels.get(position).getRoomno() + ", "
                +ordersModels.get(position).getBlockno() + ", "
                + ordersModels.get(position).getStreetno() + ", "
                + ordersModels.get(position).getArea());
        holder.cusArea.setText(ordersModels.get(position).getArea());
        holder.buildingNoAndName.setText(ordersModels.get(position).getBuildingno() + ", "
                + ordersModels.get(position).getBuildingName());
        holder.mobileAndLane.setText(ordersModels.get(position).getPhoneno() + ", " + ordersModels.get(position).getLaneLineNO());



        holder.addressCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousPosition = position;
                Gson gson = new Gson();
                Utils.storeData(holder.itemView.getContext(),gson.toJson(ordersModels.get(position)),"ADDRESS");

                Intent i = new Intent("ADDRESS");
                i.putExtra("ADDRESS", gson.toJson(ordersModels.get(position)));

                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(i);

                try {
                    sendAddressPosition = (SendAddressPosition) holder.addressCardView.getContext();
                }catch (Exception e){
                    e.printStackTrace();
                    sendAddressPosition = null;
                }
                if (sendAddressPosition != null) {
                    sendAddressPosition.sendAddressPosition(position);
                }

                notifyDataSetChanged();

            }
        });

        if(previousPosition==position){
            holder.addressCardView.setBackground(holder.addressCardView.getContext().getResources().getDrawable(R.drawable.bg_address_cartview));
        }
        else
        {
            holder.addressCardView.setBackground(holder.addressCardView.getContext().getResources().getDrawable(R.drawable.bg_tv_review));
        }

    }

    @Override
    public int getItemCount() {
        return ordersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cusName, deliveryTo, buildingNoAndName, roomNoAndBlockAndCity, cusArea, mobileAndLane;
        CardView addressCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addressCardView = itemView.findViewById(R.id.addressCardView);
            cusName = itemView.findViewById(R.id.cusName);
            deliveryTo = itemView.findViewById(R.id.deliveryTo);
            buildingNoAndName = itemView.findViewById(R.id.buildingNoAndName);
            roomNoAndBlockAndCity = itemView.findViewById(R.id.roomNoAndBlockAndCity);
            cusArea = itemView.findViewById(R.id.cusArea);
            mobileAndLane = itemView.findViewById(R.id.mobileAndLane);


        }
    }
}
