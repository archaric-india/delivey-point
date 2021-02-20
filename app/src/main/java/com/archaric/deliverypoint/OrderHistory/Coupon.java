package com.archaric.deliverypoint.OrderHistory;

public class Coupon {
    private String id;
    private String code;
    private int offer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", offer=" + offer +
                '}';
    }
}
