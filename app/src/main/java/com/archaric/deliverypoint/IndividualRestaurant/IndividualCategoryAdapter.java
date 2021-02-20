package com.archaric.deliverypoint.IndividualRestaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.SendItemData;

import java.util.ArrayList;

public class IndividualCategoryAdapter extends RecyclerView.Adapter<IndividualCategoryAdapter.ViewHolder> {

    ArrayList<IndividualResCategoryModel> individualResCategoryModels;
    RecyclerView recyclerView;
    private int previousPosition = 0;
    private boolean flagFirstItemSelected = false;
    private SendItemData sendItemData;

    public IndividualCategoryAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setIndividualResCategoryModels(ArrayList<IndividualResCategoryModel> individualResCategoryModels) {
        this.individualResCategoryModels = individualResCategoryModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_individual_res_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.categoryOfIndividualRes.setText(individualResCategoryModels.get(position).getCategory());


        if (!flagFirstItemSelected) {
            holder.categoryOfIndividualResView.setVisibility(View.VISIBLE);
            try {
                sendItemData = (SendItemData) holder.categoryOfIndividualResView.getContext();
            } catch (ClassCastException e) {
                sendItemData = null;
                e.printStackTrace();
            }
            if (sendItemData != null) {
                sendItemData.sendData(individualResCategoryModels.get(0).getItems());
            }
            flagFirstItemSelected = true;
        } else {
            holder.categoryOfIndividualResView.setVisibility(View.GONE);
        }


        holder.categoryOfIndividualResLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sendItemData = (SendItemData) holder.categoryOfIndividualResView.getContext();
                } catch (ClassCastException e) {
                    sendItemData = null;
                    e.printStackTrace();
                }
                if (sendItemData != null) {
                    sendItemData.sendData(individualResCategoryModels.get(position).getItems());
                }

                previousPosition = position;
                notifyDataSetChanged();

            }
        });

        if(previousPosition==position){
            holder.categoryOfIndividualResView.setVisibility(View.VISIBLE);
            holder.categoryOfIndividualRes.setTextColor(holder.categoryOfIndividualResView.getContext().getResources().getColor(R.color.purple));
        }
        else
        {
            holder.categoryOfIndividualResView.setVisibility(View.GONE);
            holder.categoryOfIndividualRes.setTextColor(holder.categoryOfIndividualResView.getContext().getResources().getColor(R.color.black));

        }

    }



    @Override
    public int getItemCount() {
        return individualResCategoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout categoryOfIndividualResLayout;
        TextView categoryOfIndividualRes;
        View categoryOfIndividualResView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryOfIndividualResLayout = itemView.findViewById(R.id.categoryOfIndividualResLayout);
            categoryOfIndividualRes = itemView.findViewById(R.id.categoryOfIndividualRes);
            categoryOfIndividualResView = itemView.findViewById(R.id.categoryOfIndividualResView);


        }
    }
}
