package com.archaric.deliverypoint.OrderHistory;

import com.archaric.deliverypoint.ZoneModel;
import com.archaric.deliverypoint.location;

public class DeliveryModel {

    String id ;
    String name;
    String phone;
    com.archaric.deliverypoint.location location;
    String password;
    float rating;
    String ratinglen, profile;


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getRatinglen() {
        return ratinglen;
    }

    public void setRatinglen(String ratinglen) {
        this.ratinglen = ratinglen;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public com.archaric.deliverypoint.location getLocation() {
        return location;
    }

    public void setLocation(com.archaric.deliverypoint.location location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DeliveryModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", location=" + location +
                ", password='" + password + '\'' +
                '}';
    }
}

