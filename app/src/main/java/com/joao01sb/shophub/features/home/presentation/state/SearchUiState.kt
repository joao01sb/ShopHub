package com.joao01sb.shophub.features.home.presentation.state

import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.state.SearchState

data class SearchUiState(
    val searchQuery: String = "",
    val recentSearches: List<String> = emptyList(),
    val searchResults: List<Product> = emptyList(),
    val isSearching: Boolean = false,
    val searchState: SearchState = SearchState.RECENT_SEARCHES,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMoreResults: Boolean = true,
    val isLoadingMore: Boolean = false
)