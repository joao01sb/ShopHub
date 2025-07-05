package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.usecase.SearchProductsUseCase
import com.joao01sb.shophub.features.home.presentation.event.SearchEvent
import com.joao01sb.shophub.features.home.presentation.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.QueryChanged -> {
                updateSearchQuery(event.query)
            }
            is SearchEvent.Search -> {
                performSearch()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<Product>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchProductsUseCase(query)
            }
        }
        .cachedIn(viewModelScope)

    private fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isNotBlank()) {
            _uiState.value = _uiState.value.copy(isSearching = true)
        }
    }
}