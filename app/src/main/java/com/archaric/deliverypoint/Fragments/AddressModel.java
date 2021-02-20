package com.archaric.deliverypoint.Fragments;

public class AddressModel {

    String cusName;
    String buildingNo, locality, city, country, pincode, wholeAddress;
    Double lat, lng;


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public AddressModel(String doorNo, String locality, String city, String country, String pincode, String wholeAddress, Double lat,Double lng) {
        this.buildingNo = doorNo;
        this.locality = locality;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
        this.wholeAddress = wholeAddress;
        this.lat = lat;
        this.lng = lng;
    }

    public String getWholeAddress() {
        return wholeAddress;
    }

    public void setWholeAddress(String wholeAddress) {
        this.wholeAddress = wholeAddress;
    }

    public AddressModel(String cusName) {
        this.cusName = cusName;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "cusName='" + cusName + '\'' +
                '}';
    }
}
