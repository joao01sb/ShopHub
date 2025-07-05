package com.joao01sb.shophub.features.home.presentation.state

data class SearchUiState(
    val isSearching: Boolean = false,
    val error: String? = null
)