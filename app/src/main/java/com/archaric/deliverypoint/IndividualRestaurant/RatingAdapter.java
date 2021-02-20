package com.archaric.deliverypoint.IndividualRestaurant;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {


    OverAllRatingModel ratingModels;

    public void setRatingModels(OverAllRatingModel ratingModels) {
        this.ratingModels = ratingModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_individual_res,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.ratingBarOfResReviews.setRating(ratingModels.getRatings().get(position).getValue());
        holder.reviewNote.setText(ratingModels.getRatings().get(position).getNote());
        DateFormat formatter = new SimpleDateFormat("d MMM yyyy");

        long milliSeconds= Long.parseLong(ratingModels.getRatings().get(position).getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        holder.reviewTime.setText(formatter.format(calendar.getTime()));

        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(holder.itemImageReview.getContext())
                .load(ratingModels.getRatings().get(position).getImage())
                .apply(reqOpt)
                .into(holder.itemImageReview);

    }

    @Override
    public int getItemCount() {
        return ratingModels.getRatings().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBarOfResReviews;
        TextView reviewNote, reviewTime;
        ImageView itemImageReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingBarOfResReviews = itemView.findViewById(R.id.ratingBarOfResReviews);
            reviewNote = itemView.findViewById(R.id.reviewNote);
            reviewTime = itemView.findViewById(R.id.reviewTime);
            itemImageReview = itemView.findViewById(R.id.itemImageReview);



        }
    }
}
