package com.archaric.deliverypoint.IndividualRestaurant;

import java.util.List;

public class OverAllRatingModel {

    float or,qof,d,vfm;
    List<RatingModel> ratings;


    public OverAllRatingModel(float or, float qof, float d, float vfm, List<RatingModel> ratings) {
        this.or = or;
        this.qof = qof;
        this.d = d;
        this.vfm = vfm;
        this.ratings = ratings;
    }

    public float getOr() {
        return or;
    }

    public void setOr(float or) {
        this.or = or;
    }

    public float getQof() {
        return qof;
    }

    public void setQof(float qof) {
        this.qof = qof;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public float getVfm() {
        return vfm;
    }

    public void setVfm(float vfm) {
        this.vfm = vfm;
    }

    public List<RatingModel> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingModel> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "OverAllRatingModel{" +
                "or=" + or +
                ", qof=" + qof +
                ", d=" + d +
                ", vfm=" + vfm +
                ", ratings=" + ratings +
                '}';
    }
}
