package com.archaric.deliverypoint.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.ViewHolder> {

    ArrayList<AllCategoriesModel> modelArrayList;

    public void setModelArrayList(ArrayList<AllCategoriesModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offers_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(holder.specialOffersImageView.getContext())
                .load(modelArrayList.get(position).getImage())
                .apply(reqOpt)
                .into(holder.specialOffersImageView);

        holder.specialOffersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FiftyPercentOfferModel percentOfferModelNew =
                        new FiftyPercentOfferModel(modelArrayList.get(position).getRestuarants().getId(),modelArrayList.get(position).getRestuarants().getName()
                                ,modelArrayList.get(position).getRestuarants().getProfile(),modelArrayList.get(position).getRestuarants().getCategory(),modelArrayList.get(position).getRestuarants().getRatinglenght()
                                ,modelArrayList.get(position).getRestuarants().getTimevalue(),modelArrayList.get(position).getRestuarants().getLogo(),modelArrayList.get(position).getRestuarants().getRating()
                                ,modelArrayList.get(position).getRestuarants().getOffer());
                Intent intent = new Intent(holder.specialOffersImageView.getContext(), IndividualRestaurant.class);
                intent.putExtra(RES_DATA, percentOfferModelNew);
                holder.specialOffersImageView.getContext().startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView specialOffersImageView;
        CardView specialOffersCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            specialOffersImageView = itemView.findViewById(R.id.specialOffersImageView);
            specialOffersCardView = itemView.findViewById(R.id.specialOffersCardView);
        }
    }
}
