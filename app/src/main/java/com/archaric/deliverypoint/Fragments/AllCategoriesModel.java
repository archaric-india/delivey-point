package com.archaric.deliverypoint.Fragments;

public class AllCategoriesModel {

    String name, image;
    FiftyPercentOfferModel restuarants;

    public String getCategory() {
        return name;
    }

    public void setCategory(String category) {
        this.name = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FiftyPercentOfferModel getRestuarants() {
        return restuarants;
    }

    public void setRestuarants(FiftyPercentOfferModel restuarants) {
        this.restuarants = restuarants;
    }

    public AllCategoriesModel(String category, String image, FiftyPercentOfferModel restuarants) {
        this.name = category;
        this.image = image;
        this.restuarants = restuarants;
    }

    @Override
    public String toString() {
        return "AllCategoriesModel{" +
                "category='" + name + '\'' +
                ", image='" + image + '\'' +
                ", restuarants=" + restuarants +
                '}';
    }
}
