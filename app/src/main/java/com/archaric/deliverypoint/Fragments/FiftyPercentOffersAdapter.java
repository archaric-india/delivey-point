package com.archaric.deliverypoint.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.IndividualRestaurant.IndividualRestaurant;
import com.archaric.deliverypoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FiftyPercentOffersAdapter extends RecyclerView.Adapter<FiftyPercentOffersAdapter.ViewHolder> {
    public static final String RES_DATA = "resData";

    ArrayList<FiftyPercentOfferModel> fiftyPercentOfferModels;

    public void setFiftyPercentOfferModels(ArrayList<FiftyPercentOfferModel> fiftyPercentOfferModels) {
        this.fiftyPercentOfferModels = fiftyPercentOfferModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fifty_percentange_offers_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.itemNameOfFiftyPerOffers.setText(fiftyPercentOfferModels.get(position).getName());
        holder.categoryOfFiftyPerOffers.setText(fiftyPercentOfferModels.get(position).getCategory());
        holder.offerPercentage.setText(String.valueOf(fiftyPercentOfferModels.get(position).getOffer()) +" % OFF");
        holder.priceFiftyPerOff.setText("Delivery Fee : KWD " + String.valueOf(fiftyPercentOfferModels.get(position).getDelivery_fee()));
        holder.ratingBar.setRating(fiftyPercentOfferModels.get(position).getRating());
        holder.totalNumOfReview.setText("("+ fiftyPercentOfferModels.get(position).getRatinglenght() +")");
        holder.estimatedDeliveryTime.setText(fiftyPercentOfferModels.get(position).getTimevalue());

        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(holder.fiftyPerOffersIV.getContext())
                .load(fiftyPercentOfferModels.get(position).getProfile())
                .apply(reqOpt)
                .into(holder.fiftyPerOffersIV);



        holder.fiftyPerOffersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiftyPercentOfferModel percentOfferModelNew =
                        new FiftyPercentOfferModel(fiftyPercentOfferModels.get(position).getId(),fiftyPercentOfferModels.get(position).getName()
                                ,fiftyPercentOfferModels.get(position).getProfile(),fiftyPercentOfferModels.get(position).getCategory(),fiftyPercentOfferModels.get(position).getRatinglenght()
                                ,fiftyPercentOfferModels.get(position).getTimevalue(),fiftyPercentOfferModels.get(position).getLogo(),fiftyPercentOfferModels.get(position).getRating(),fiftyPercentOfferModels.get(position).getOffer());
                Intent intent = new Intent(holder.fiftyPerOffersIV.getContext(), IndividualRestaurant.class);
                intent.putExtra(RES_DATA, percentOfferModelNew);
                holder.fiftyPerOffersIV.getContext().startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return fiftyPercentOfferModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemNameOfFiftyPerOffers, categoryOfFiftyPerOffers, estimatedDeliveryTime, offerPercentage, priceFiftyPerOff, totalNumOfReview;
        RatingBar ratingBar;
        ImageView fiftyPerOffersIV;
        CardView fiftyPerOffersCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameOfFiftyPerOffers = itemView.findViewById(R.id.itemNameOfFiftyPerOffers);
            categoryOfFiftyPerOffers = itemView.findViewById(R.id.categoryOfFiftyPerOffers);
            estimatedDeliveryTime = itemView.findViewById(R.id.estimatedDeliveryTime);
            offerPercentage = itemView.findViewById(R.id.offerPercentage);
            totalNumOfReview = itemView.findViewById(R.id.totalNumOfReview);

            ratingBar = itemView.findViewById(R.id.ratingBar);

            fiftyPerOffersIV = itemView.findViewById(R.id.fiftyPerOffersIV);

            priceFiftyPerOff = itemView.findViewById(R.id.priceFiftyPerOff);
            fiftyPerOffersCardView = itemView.findViewById(R.id.fiftyPerOffersCardView);


        }
    }
}
