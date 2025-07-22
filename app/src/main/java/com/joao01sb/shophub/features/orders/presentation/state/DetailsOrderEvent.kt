package com.joao01sb.shophub.features.orders.presentation.state

sealed class DetailsOrderEvent {
    object RefreshOrder : DetailsOrderEvent()
}