package com.joao01sb.shophub.features.home.presentation.state

import com.joao01sb.shophub.core.domain.model.Product

data class ProductDetailsUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)