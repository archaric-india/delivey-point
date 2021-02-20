package com.archaric.deliverypoint.OrderHistory;

public class DeliveryModel {

    String id ;
    String name;
    String phone;
    com.archaric.deliverypoint.location location;
    String password;

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

