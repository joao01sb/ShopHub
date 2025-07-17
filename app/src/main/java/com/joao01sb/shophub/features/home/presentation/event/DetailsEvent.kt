package com.joao01sb.shophub.features.home.presentation.event

sealed interface DetailsEvent {

    data object AddToCart : DetailsEvent

}