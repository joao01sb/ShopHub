package com.joao01sb.shophub.features.home.presentation.event

sealed class DetailsUiEvent {
    data class Success(val message: String) : DetailsUiEvent()
    data object NavigateToCart : DetailsUiEvent()
}