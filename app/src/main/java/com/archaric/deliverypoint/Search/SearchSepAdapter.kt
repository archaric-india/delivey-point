package com.archaric.deliverypoint.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel
import com.archaric.deliverypoint.R

class SearchSepAdapter: RecyclerView.Adapter<SearchSepAdapter.ViewHolder>() {
    val list: ArrayList<FiftyPercentOfferModel>  = ArrayList()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fifty_percentange_offers_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int { return list.size }
}