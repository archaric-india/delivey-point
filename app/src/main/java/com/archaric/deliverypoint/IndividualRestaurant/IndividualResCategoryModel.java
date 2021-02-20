package com.archaric.deliverypoint.IndividualRestaurant;

import java.util.List;

public class IndividualResCategoryModel {

    String category;
    List<Items> items;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
