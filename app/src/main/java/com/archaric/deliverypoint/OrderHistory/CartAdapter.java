package com.archaric.deliverypoint.OrderHistory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.SendCartItemsTotal;
import com.archaric.deliverypoint.SendItemData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<Items> itemsArrayList;
    ArrayList<Items> itemsArrayList2;
    SendCartItemsTotal sendCartItemsTotal;

    CartAdapter(SendCartItemsTotal sendCartItemsTotal){
        this.sendCartItemsTotal = sendCartItemsTotal;
    }

    boolean read = false;
    int i = 0;
    double tt = 0 , db;

    public void setItemsArrayList(ArrayList<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.resultQuantity.setText(itemsArrayList.get(position).getQuantityCount());


        refreshTotal(Integer.valueOf(holder.resultQuantity.getText().toString()),itemsArrayList.get(position).getPrice());


        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(holder.itemImage.getContext())
                .load(itemsArrayList.get(position).getImage())
                .apply(reqOpt)
                .into(holder.itemImage);

        holder.itemName.setText(itemsArrayList.get(position).getName());
        holder.itemPrice.setText(String.valueOf(itemsArrayList.get(position).getPrice()));


        holder.addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = Integer.valueOf(holder.resultQuantity.getText().toString());

                System.out.println(i + "I value in ADD");
                if (i<10){
                    i = i+1;
                    itemsArrayList.get(position).setQuantityCount(String.valueOf(i));
                    update(holder.resultQuantity.getContext());
                    notifyDataSetChanged();
                    db = 0;
                    holder.resultQuantity.setText(String.valueOf(i));

                }else {
                    Toast.makeText( holder.resultQuantity.getContext(), "Delivery Limit 10 Only", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = Integer.valueOf(holder.resultQuantity.getText().toString());

                System.out.println(i + "I value in minus");
                if (i > 1) {
                    i = i - 1;
                    itemsArrayList.get(position).setQuantityCount(String.valueOf(i));
                    update(holder.resultQuantity.getContext());
                    notifyDataSetChanged();
                    db = 0;
                    holder.resultQuantity.setText(String.valueOf(i));
                }
            }
        });

        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(holder.removeFromCart.getContext(), itemsArrayList.get(position).getName() + " is Removed", Toast.LENGTH_SHORT).show();
                itemsArrayList.remove(position);
                if(itemsArrayList.size() == 0){
                    db = 0;
                    refreshTotal( 0, 0);
                }
                notifyDataSetChanged();


                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(holder.removeFromCart.getContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson2 = new Gson();
                String json2 = sharedPrefs.getString("TAG", "[]");
                Type type2 = new TypeToken<List<Items>>() {}.getType();

                if (db == 0){
                    ArrayList<Items> newArray = new ArrayList<>();
                    json2 = gson2.toJson(newArray);
                }else {
                    json2 = gson2.toJson(itemsArrayList);
                }
                editor.putString("TAG", json2);
                System.out.println(json2);
                editor.commit();


                //itemsArrayList2.addAll(arrayList);




            }
        });



    }

    public void update(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);


        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson2 = new Gson();

        String json2 = sharedPrefs.getString("TAG", "[]");
        Type type2 = new TypeToken<List<Items>>() {}.getType();
        //itemsArrayList2.addAll(arrayList);
        json2 = gson2.toJson(itemsArrayList);
        editor.putString("TAG", json2);
        System.out.println(json2);
        editor.commit();
    }

    public void refreshTotal(int val, float fl){
            db = db + (val * fl);

        System.out.println(db + "inside ref");
        sendCartItemsTotal.cartTotal((float)db,itemsArrayList.size());
    }


    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage, minusQuantity, addQuantity, removeFromCart;
        TextView itemName, itemPrice, resultQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            minusQuantity = itemView.findViewById(R.id.minusQuantity);
            addQuantity = itemView.findViewById(R.id.addQuantity);
            removeFromCart = itemView.findViewById(R.id.removeFromCart);

            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            resultQuantity = itemView.findViewById(R.id.resultQuantity);

        }
    }
}
