package com.joao01sb.shophub.features.home.presentation.state

import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.enum.SearchState

data class SearchUiState(
    val searchQuery: String = "",
    val recentSearches: List<String> = emptyList(),
    val isSearching: Boolean = false,
    val searchState: SearchState = SearchState.RECENT_SEARCHES,
    val error: String? = null
)