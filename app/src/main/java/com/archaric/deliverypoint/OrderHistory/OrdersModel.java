package com.archaric.deliverypoint.OrderHistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.IndividualRestaurant.Items;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class OrdersModel implements Serializable {

    String id, type, method, deliveryto, blockno, streetno, streetName, buildingno, roomno,
            jeddah, area, landmark, phoneno, description, deliverydesc, date, buildingName,
            assignedto, datef, rid, status, placedtime, completedtime, uid, customername, address, laneLineNO, avenue, addressTitle;
    Float subtotal, deliveryfee, total;

    boolean pickup, delivered;
    double exptime;
    com.archaric.deliverypoint.location location;
    List<Items> items;
    FiftyPercentOfferModel restaurant;
    Coupon coupon;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public FiftyPercentOfferModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(FiftyPercentOfferModel restaurant) {
        this.restaurant = restaurant;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAvenue() {
        return avenue;
    }

    public void setAvenue(String avenue) {
        this.avenue = avenue;
    }

    public String getLaneLineNO() {
        return laneLineNO;
    }

    public void setLaneLineNO(String laneLineNO) {
        this.laneLineNO = laneLineNO;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDeliveryto() {
        return deliveryto;
    }

    public void setDeliveryto(String deliveryto) {
        this.deliveryto = deliveryto;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    public String getStreetno() {
        return streetno;
    }

    public void setStreetno(String streetno) {
        this.streetno = streetno;
    }

    public String getBuildingno() {
        return buildingno;
    }

    public void setBuildingno(String buildingno) {
        this.buildingno = buildingno;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getJedda() {
        return jeddah;
    }

    public void setJedda(String jedda) {
        this.jeddah = jedda;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliverydesc() {
        return deliverydesc;
    }

    public void setDeliverydesc(String deliverydesc) {
        this.deliverydesc = deliverydesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(String assignedto) {
        this.assignedto = assignedto;
    }

    public String getDatef() {
        return datef;
    }

    public void setDatef(String datef) {
        this.datef = datef;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlacedtime() {
        return placedtime;
    }

    public void setPlacedtime(String placedtime) {
        this.placedtime = placedtime;
    }

    public String getCompletedtime() {
        return completedtime;
    }

    public void setCompletedtime(String completedtime) {
        this.completedtime = completedtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(Float deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public double getExptime() {
        return exptime;
    }

    public void setExptime(double exptime) {
        this.exptime = exptime;
    }

    public com.archaric.deliverypoint.location getLocation() {
        return location;
    }

    public void setLocation(com.archaric.deliverypoint.location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersModel that = (OrdersModel) o;
        return
                Objects.equals(id, that.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, method, deliveryto, blockno, streetno, streetName, buildingno, roomno, jeddah, area, landmark, phoneno, description, deliverydesc, date, buildingName, assignedto, datef, rid, status, placedtime, completedtime, uid, customername, address, laneLineNO, avenue, addressTitle, subtotal, deliveryfee, total, pickup, delivered, exptime, location, items);
    }

    @Override
    public String toString() {
        return "OrdersModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", method='" + method + '\'' +
                ", deliveryto='" + deliveryto + '\'' +
                ", blockno='" + blockno + '\'' +
                ", streetno='" + streetno + '\'' +
                ", streetName='" + streetName + '\'' +
                ", buildingno='" + buildingno + '\'' +
                ", roomno='" + roomno + '\'' +
                ", jeddah='" + jeddah + '\'' +
                ", area='" + area + '\'' +
                ", landmark='" + landmark + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", description='" + description + '\'' +
                ", deliverydesc='" + deliverydesc + '\'' +
                ", date='" + date + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", assignedto='" + assignedto + '\'' +
                ", datef='" + datef + '\'' +
                ", rid='" + rid + '\'' +
                ", status='" + status + '\'' +
                ", placedtime='" + placedtime + '\'' +
                ", completedtime='" + completedtime + '\'' +
                ", uid='" + uid + '\'' +
                ", customername='" + customername + '\'' +
                ", address='" + address + '\'' +
                ", laneLineNO='" + laneLineNO + '\'' +
                ", avenue='" + avenue + '\'' +
                ", addressTitle='" + addressTitle + '\'' +
                ", subtotal=" + subtotal +
                ", deliveryfee=" + deliveryfee +
                ", total=" + total +
                ", pickup=" + pickup +
                ", delivered=" + delivered +
                ", exptime=" + exptime +
                ", location=" + location +
                ", items=" + items +
                ", restaurant=" + restaurant +
                ", coupon=" + coupon +
                '}';
    }


}
