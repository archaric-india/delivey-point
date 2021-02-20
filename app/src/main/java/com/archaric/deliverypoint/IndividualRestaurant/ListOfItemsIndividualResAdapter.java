package com.archaric.deliverypoint.IndividualRestaurant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ListOfItemsIndividualResAdapter extends RecyclerView.Adapter<ListOfItemsIndividualResAdapter.ViewHolder> {

    List<Items> itemsArrayList;
    public static final String ITEM_ID_DATA = "ITEM_ID_RES";

    public void setItemsArrayList(List<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ind_item_view,parent,false);

        if(viewType == itemsArrayList.size() -1){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            params.setMargins(0,0,0,300);
            view.setLayoutParams(params);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(itemsArrayList.get(position).getName());
        holder.itemCategory.setText(itemsArrayList.get(position).getDescription());




        if (itemsArrayList.get(position).getVariations() != null) {

            holder.selectionTV.setVisibility(View.VISIBLE);
            holder.amountTV.setVisibility(View.GONE);

        }else {
            holder.amountTV.setText(String.valueOf("KD "+itemsArrayList.get(position).getPrice()));
        }


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




        holder.indCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemImage.getContext(), IndividualProductPage.class);
                Items items = new Items(itemsArrayList.get(position).getId(),itemsArrayList.get(position).getName(),
                        itemsArrayList.get(position).getStatus(),itemsArrayList.get(position).getRid(),
                        itemsArrayList.get(position).getCategory(),itemsArrayList.get(position).getBid(),
                        itemsArrayList.get(position).getImage(),itemsArrayList.get(position).getDescription(),itemsArrayList.get(position).getRating(),
                        itemsArrayList.get(position).getPrice(),itemsArrayList.get(position).getOff(),itemsArrayList.get(position).getVeg(),itemsArrayList.get(position).getAvail(),
                        itemsArrayList.get(position).getVariations(),itemsArrayList.get(position).getAddons());

                intent.putExtra(ITEM_ID_DATA, items);
                holder.itemImage.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemCategory, amountTV, selectionTV;
        ImageView itemImage;
        LinearLayout indCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.itemCategory);
             amountTV = itemView.findViewById(R.id.amountTV);
            selectionTV = itemView.findViewById(R.id.selectionTV);
            itemImage = itemView.findViewById(R.id.itemImage);
            indCardView = itemView.findViewById(R.id.indCardView);


        }
    }
}
