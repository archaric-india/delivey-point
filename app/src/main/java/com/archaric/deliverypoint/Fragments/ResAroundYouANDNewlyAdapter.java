package com.archaric.deliverypoint.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
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

import static com.archaric.deliverypoint.Fragments.FiftyPercentOffersAdapter.RES_DATA;

public class ResAroundYouANDNewlyAdapter extends RecyclerView.Adapter<ResAroundYouANDNewlyAdapter.ViewHolder> {
    ArrayList<FiftyPercentOfferModel> models;

    public void setModels(ArrayList<FiftyPercentOfferModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_around_newly_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemNameOfFiftyPerOffers.setText(models.get(position).getName());
        holder.categoryOfFiftyPerOffers.setText(models.get(position).getCategory());
        holder.priceFiftyPerOff.setText("Delivery Fee : KWD " + String.valueOf(models.get(position).getDelivery_fee()));
        holder.ratingBar.setRating(models.get(position).getRating());
        holder.estimatedDeliveryTime.setText(models.get(position).getTimevalue());
        holder.totalNumOfReview.setText("("+ models.get(position).getRatinglenght() +")");

        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(holder.fiftyPerOffersIV.getContext())
                .load(models.get(position).getProfile())
                .apply(reqOpt)
                .into(holder.fiftyPerOffersIV);

        holder.resAroundNewlyJoinedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiftyPercentOfferModel percentOfferModelNew =
                        new FiftyPercentOfferModel(models.get(position).getId(),models.get(position).getName()
                                ,models.get(position).getProfile(),models.get(position).getCategory(),models.get(position).getRatinglenght()
                                ,models.get(position).getTimevalue(),models.get(position).getLogo(),models.get(position).getRating(),models.get(position).getOffer());
                Intent intent = new Intent(holder.fiftyPerOffersIV.getContext(), IndividualRestaurant.class);
                intent.putExtra(RES_DATA, percentOfferModelNew);
                holder.fiftyPerOffersIV.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView itemNameOfFiftyPerOffers, categoryOfFiftyPerOffers, estimatedDeliveryTime, priceFiftyPerOff, totalNumOfReview;
        RatingBar ratingBar;
        ImageView fiftyPerOffersIV;
        CardView resAroundNewlyJoinedCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameOfFiftyPerOffers = itemView.findViewById(R.id.itemNameOfFiftyPerOffers);
            categoryOfFiftyPerOffers = itemView.findViewById(R.id.categoryOfFiftyPerOffers);
            estimatedDeliveryTime = itemView.findViewById(R.id.estimatedDeliveryTime);
            totalNumOfReview = itemView.findViewById(R.id.totalNumOfReview);

            ratingBar = itemView.findViewById(R.id.ratingBar);

            fiftyPerOffersIV = itemView.findViewById(R.id.fiftyPerOffersIV);

            priceFiftyPerOff = itemView.findViewById(R.id.priceFiftyPerOff);

            resAroundNewlyJoinedCardView= itemView.findViewById(R.id.resAroundNewlyJoinedCardView);

        }
    }
}
