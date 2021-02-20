package com.archaric.deliverypoint;

public class ZoneModel {

    String zone, subzone;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getSubzone() {
        return subzone;
    }

    public void setSubzone(String subzone) {
        this.subzone = subzone;
    }

    @Override
    public String toString() {
        return "ZoneModel{" +
                "zone='" + zone + '\'' +
                ", subzone='" + subzone + '\'' +
                '}';
    }
}
