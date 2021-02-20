package com.archaric.deliverypoint.IndividualRestaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Items implements Serializable {


    String id,name,status,rid,category,bid,image,description;
    float rating, price, off;
    Boolean veg, avail;
    String variationKey;
    List<String> addonsKeys;
    String specialRequest;
    String quantityCount;


    private Map<String,Double> variations;
    private Map<String, Double> addons;


    public Items(String id, String name, String status, String rid, String category, String bid, String image, String description, float rating, float price, float off, Boolean veg, Boolean avail, Map<String, Double> variations, Map<String, Double> addons) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.rid = rid;
        this.category = category;
        this.bid = bid;
        this.image = image;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.off = off;
        this.veg = veg;
        this.avail = avail;
        this.variations = variations;
        this.addons = addons;
    }

    public Items(String id, String name, String image, float price, String variationKey, List<String> addonsKeys,String specialRequest,String quantityCount, String bid) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.variationKey = variationKey;
        this.addonsKeys = addonsKeys;
        this.specialRequest = specialRequest;
        this.quantityCount = quantityCount;
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "ItemsNew{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", rid='" + rid + '\'' +
                ", category='" + category + '\'' +
                ", bid='" + bid + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", off=" + off +
                ", veg=" + veg +
                ", avail=" + avail +
                ", variations=" + variations +
                ", addons=" + addons +
                '}';
    }

    public String getVariationKey() {
        return variationKey;
    }

    public void setVariationKey(String variationKey) {
        this.variationKey = variationKey;
    }

    public List<String> getAddonsKeys() {
        return addonsKeys;
    }

    public void setAddonsKeys(List<String> addonsKeys) {
        this.addonsKeys = addonsKeys;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getQuantityCount() {
        return quantityCount;
    }

    public void setQuantityCount(String quantityCount) {
        this.quantityCount = quantityCount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getOff() {
        return off;
    }

    public void setOff(float off) {
        this.off = off;
    }

    public Boolean getVeg() {
        return veg;
    }

    public void setVeg(Boolean veg) {
        this.veg = veg;
    }

    public Boolean getAvail() {
        return avail;
    }

    public void setAvail(Boolean avail) {
        this.avail = avail;
    }

    public Map<String, Double> getVariations() {
        return variations;
    }

    public void setVariations(Map<String, Double> variations) {
        this.variations = variations;
    }

    public Map<String, Double> getAddons() {
        return addons;
    }

    public void setAddons(Map<String, Double> addons) {
        this.addons = addons;
    }
}
