package com.joao01sb.shophub.features.orders.presentation.state

sealed class OrdersEvent {
        object RefreshOrders : OrdersEvent()
        object Logout : OrdersEvent()
    }