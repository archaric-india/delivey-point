package com.archaric.deliverypoint.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.IndividualRestaurant.IndividualRestaurant;
import com.archaric.deliverypoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder> {

    ArrayList<AllCategoriesModel> allCategoriesModels;


    public void setAllCategoriesModels(ArrayList<AllCategoriesModel> allCategoriesModels) {
        this.allCategoriesModels = allCategoriesModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_categories_view,parent,false);
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

        holder.categoryName.setText(allCategoriesModels.get(position).getCategory());
        Glide.with(holder.imageOfCategory.getContext())
                .load(allCategoriesModels.get(position).getImage())
                .apply(reqOpt)
                .into(holder.imageOfCategory);

    }

    @Override
    public int getItemCount() {
        return allCategoriesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout categoryOfAllLayouts;
        ImageView imageOfCategory;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryOfAllLayouts = itemView.findViewById(R.id.categoryOfAllLayouts);
            imageOfCategory = itemView.findViewById(R.id.imageOfCategory);
            categoryName = itemView.findViewById(R.id.categoryName);


        }
    }
}
