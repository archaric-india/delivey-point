package com.archaric.deliverypoint.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.OrderHistory.ServerResponse;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    public static final String ADDRESS_DATA = "Address Data" ;
    ArrayList<OrdersModel> ordersModelArrayList;

    public void setOrdersModelArrayList(ArrayList<OrdersModel> ordersModelArrayList) {
        this.ordersModelArrayList = ordersModelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cusName.setText(ordersModelArrayList.get(position).getCustomername());
        holder.deliveryto.setText(ordersModelArrayList.get(position).getDeliveryto());
        holder.roomNoAndBlockAndCity.setText(ordersModelArrayList.get(position).getRoomno() + ", "
                +ordersModelArrayList.get(position).getBlockno() + ", "
                + ordersModelArrayList.get(position).getStreetno() + ", "
                + ordersModelArrayList.get(position).getArea());
        holder.cusCity.setText(ordersModelArrayList.get(position).getArea());
        holder.buildingNoAndName.setText(ordersModelArrayList.get(position).getBuildingno() + ", "
                + ordersModelArrayList.get(position).getBuildingName());
        holder.mobileAndLane.setText(ordersModelArrayList.get(position).getPhoneno() + ", " + ordersModelArrayList.get(position).getLaneLineNO());

        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(holder.moreOptions.getContext(), holder.moreOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.popup_menu_add_edit);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                OrdersModel ordersModel = ordersModelArrayList.get(position);
                                Intent intent = new Intent(holder.moreOptions.getContext(),AddNewAddress.class);
                                intent.putExtra("AddAddress","Edit Address");
                                intent.putExtra(ADDRESS_DATA, ordersModel);
                                holder.moreOptions.getContext().startActivity(intent);
                                return true;
                            case R.id.remove:
                                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            System.out.println(ordersModelArrayList.get(position).getId());
                                            removeAddress(ordersModelArrayList.get(position).getId(), holder.moreOptions.getContext());
                                            ordersModelArrayList.remove(position);
                                            notifyDataSetChanged();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.moreOptions.getContext());
                                builder.setMessage("Are you sure you want to remove this address?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cusName, deliveryto, buildingNoAndName, roomNoAndBlockAndCity, cusCity, mobileAndLane;
        ImageView moreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cusName = itemView.findViewById(R.id.cusName);
            deliveryto = itemView.findViewById(R.id.deliveryto);
            buildingNoAndName = itemView.findViewById(R.id.buildingNoAndName);
            roomNoAndBlockAndCity = itemView.findViewById(R.id.roomNoAndBlockAndCity);
            cusCity = itemView.findViewById(R.id.cusCity);
            mobileAndLane = itemView.findViewById(R.id.mobileAndLane);
            moreOptions  = itemView.findViewById(R.id.moreOptions);

        }
    }

    private void removeAddress(String addressId, Context context){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.removeUserAddress(addressId).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Utils.toast(context,"Address Removed!");
                }


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Utils.toast(context,t.getMessage().toString());
            }
        });
    }
}
