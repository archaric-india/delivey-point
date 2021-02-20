package com.archaric.deliverypoint.Fragments;

import com.archaric.deliverypoint.OrderHistory.OrdersModel;

public abstract class SetPlaceOrderData {

    static OrdersModel ordersModel;

    public static OrdersModel getOrdersModel() {
        return ordersModel;
    }

    public static void setOrdersModel(OrdersModel ordersModel) {
        SetPlaceOrderData.ordersModel = ordersModel;
    }
}
