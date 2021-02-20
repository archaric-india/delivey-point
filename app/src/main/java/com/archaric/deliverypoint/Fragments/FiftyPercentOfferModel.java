package com.archaric.deliverypoint.Fragments;

import com.archaric.deliverypoint.location;

import java.io.Serializable;

public class FiftyPercentOfferModel implements Serializable {
   String id, name, profile, category, ratinglenght, timevalue, logo, address, landmark, phone, zone;
   float rating, preptime;
   double delivery_fee;
   int offer;
   com.archaric.deliverypoint.location location;

    public com.archaric.deliverypoint.location getLocation() {
        return location;
    }

    public void setLocation(com.archaric.deliverypoint.location location) {
        this.location = location;
    }

    public double getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public FiftyPercentOfferModel(String id, String name, String profile, String category, String ratinglenght, String timevalue, String logo, float rating, int offer) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.category = category;
        this.ratinglenght = ratinglenght;
        this.timevalue = timevalue;
        this.logo = logo;
        this.rating = rating;
        this.offer = offer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public float getPreptime() {
        return preptime;
    }

    public void setPreptime(float preptime) {
        this.preptime = preptime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRatinglenght() {
        return ratinglenght;
    }

    public void setRatinglenght(String ratinglenght) {
        this.ratinglenght = ratinglenght;
    }

    public String getTimevalue() {
        return timevalue;
    }

    public void setTimevalue(String timevalue) {
        this.timevalue = timevalue;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "FiftyPercentOfferModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", category='" + category + '\'' +
                ", ratinglenght='" + ratinglenght + '\'' +
                ", timevalue='" + timevalue + '\'' +
                ", logo='" + logo + '\'' +
                ", address='" + address + '\'' +
                ", landmark='" + landmark + '\'' +
                ", phone='" + phone + '\'' +
                ", zone='" + zone + '\'' +
                ", rating=" + rating +
                ", preptime=" + preptime +
                ", delivery_fee=" + delivery_fee +
                ", offer=" + offer +
                ", location=" + location +
                '}';
    }
}
