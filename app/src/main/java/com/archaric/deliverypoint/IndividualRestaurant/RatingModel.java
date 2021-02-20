package com.archaric.deliverypoint.IndividualRestaurant;

public class RatingModel {

    String id,note,itemid,bid,date,image,uid;
    float value;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RatingModel{" +
                "id='" + id + '\'' +
                ", note='" + note + '\'' +
                ", itemid='" + itemid + '\'' +
                ", bid='" + bid + '\'' +
                ", date='" + date + '\'' +
                ", image='" + image + '\'' +
                ", uid='" + uid + '\'' +
                ", value=" + value +
                '}';
    }
}
