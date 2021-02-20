package com.archaric.deliverypoint
import com.archaric.deliverypoint.OrderHistory.OrdersModel
import java.io.Serializable
class location : Serializable {
    var lat: Double ?= null
    var long: Double ?= null
    var rot: Number ?= null

    override fun toString(): String {
        return "location(lat=$lat, long=$long, rot=$rot)"
    }
}

var currentAddress: OrdersModel = OrdersModel();