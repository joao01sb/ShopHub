package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.enum.SearchState
import com.joao01sb.shophub.features.home.domain.usecase.GetRecentSearchesUseCase
import com.joao01sb.shophub.features.home.domain.usecase.SaveRecentSearchUseCase
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val recentSearchesUseCase: GetRecentSearchesUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()


    init {
        loadRecentSearches()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                updateSearchQuery(event.query)
            }

            is SearchEvent.RecentSearchClicked -> {
                updateSearchQuery(event.search)
            }

            is SearchEvent.Search -> {
                performSearch()
            }

            is SearchEvent.Retry -> {
                performSearch()
            }

            is SearchEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<Product>> = _uiState
        .map { it.searchQuery }
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    searchState = SearchState.RECENT_SEARCHES,
                    isSearching = false,
                    error = null
                )
                flowOf(PagingData.empty())
            } else if (query.length >= 2) {
                _uiState.value = _uiState.value.copy(
                    searchState = SearchState.SEARCHING,
                    isSearching = true,
                    error = null
                )
                searchProductsUseCase(query)
                    .catch { e ->
                        _uiState.value = _uiState.value.copy(
                            searchState = SearchState.ERROR,
                            isSearching = false,
                            error = e.message ?: "Unknow error"
                        )
                        emit(PagingData.empty())
                    }
            } else {
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)

    private fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    private fun performSearch() {
        val currentQuery = _uiState.value.searchQuery
        if (currentQuery.isNotBlank()) {
            _uiState.value = _uiState.value.copy(
                isSearching = true,
                searchState = SearchState.SEARCHING,
                error = null
            )
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            try {
                recentSearchesUseCase()
                    .onSuccess { recentSearches ->
                        _uiState.value = _uiState.value.copy(recentSearches = recentSearches.map { it.query })
                    }
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(recentSearches = emptyList())
            }
        }
    }

    fun handleSearchResults(hasResults: Boolean) {
        val currentQuery = _uiState.value.searchQuery

        _uiState.value = _uiState.value.copy(
            searchState = if (hasResults) {
                SearchState.RESULTS
            } else {
                SearchState.EMPTY_RESULTS
            },
            isSearching = false
        )

        if (hasResults && currentQuery.isNotBlank()) {
            saveRecentSearch(currentQuery)
        }
    }

    private fun saveRecentSearch(query: String) {
        viewModelScope.launch {
            saveRecentSearchUseCase(query)
            loadRecentSearches()
        }
    }
}