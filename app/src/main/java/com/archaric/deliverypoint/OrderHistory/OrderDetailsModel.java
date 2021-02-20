package com.archaric.deliverypoint.OrderHistory;

public class OrderDetailsModel {
    String id,type,method,deliveryto, blockno, streetno, buildingno, jeddah, area,
            phoneno, description, date, assignedto, pickup, rid, delivered, status,
            placedtime, completedtime,uid, customername, landmark,exptime, address, roomno, deliverydesc, datef, health, gid;
    double subtotal, deliveryfee, total;


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

    public String getJeddah() {
        return jeddah;
    }

    public void setJeddah(String jeddah) {
        this.jeddah = jeddah;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
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

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getExptime() {
        return exptime;
    }

    public void setExptime(String exptime) {
        this.exptime = exptime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getDeliverydesc() {
        return deliverydesc;
    }

    public void setDeliverydesc(String deliverydesc) {
        this.deliverydesc = deliverydesc;
    }

    public String getDatef() {
        return datef;
    }

    public void setDatef(String datef) {
        this.datef = datef;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(double deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetailsModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", method='" + method + '\'' +
                ", deliveryto='" + deliveryto + '\'' +
                ", blockno='" + blockno + '\'' +
                ", streetno='" + streetno + '\'' +
                ", buildingno='" + buildingno + '\'' +
                ", jeddah='" + jeddah + '\'' +
                ", area='" + area + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", assignedto='" + assignedto + '\'' +
                ", pickup='" + pickup + '\'' +
                ", rid='" + rid + '\'' +
                ", delivered='" + delivered + '\'' +
                ", status='" + status + '\'' +
                ", placedtime='" + placedtime + '\'' +
                ", completedtime='" + completedtime + '\'' +
                ", uid='" + uid + '\'' +
                ", customername='" + customername + '\'' +
                ", landmark='" + landmark + '\'' +
                ", exptime='" + exptime + '\'' +
                ", address='" + address + '\'' +
                ", roomno='" + roomno + '\'' +
                ", deliverydesc='" + deliverydesc + '\'' +
                ", datef='" + datef + '\'' +
                ", health='" + health + '\'' +
                ", gid='" + gid + '\'' +
                ", subtotal=" + subtotal +
                ", deliveryfee=" + deliveryfee +
                ", total=" + total +
                '}';
    }
}
