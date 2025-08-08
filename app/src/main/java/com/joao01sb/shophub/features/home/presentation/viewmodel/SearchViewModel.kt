package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.state.SearchState
import com.joao01sb.shophub.features.home.domain.usecase.GetRecentSearchesUseCase
import com.joao01sb.shophub.features.home.domain.usecase.SaveRecentSearchUseCase
import com.joao01sb.shophub.features.home.domain.usecase.SearchProductsUseCase
import com.joao01sb.shophub.features.home.presentation.event.SearchEvent
import com.joao01sb.shophub.features.home.presentation.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val recentSearchesUseCase: GetRecentSearchesUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadRecentSearches()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                updateSearchQuery(event.query)
                performSearchWithDebounce()
            }

            is SearchEvent.RecentSearchClicked -> {
                updateSearchQuery(event.search)
                performSearch(resetResults = true)
            }

            is SearchEvent.Search -> {
                performSearch(resetResults = true)
            }

            is SearchEvent.LoadMore -> {
                loadMoreResults()
            }

            is SearchEvent.Retry -> {
                performSearch(resetResults = true)
            }

            is SearchEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchState = SearchState.RECENT_SEARCHES,
                searchResults = emptyList(),
                isSearching = false,
                error = null
            )
        }
    }

    private fun performSearchWithDebounce() {
        searchJob?.cancel()

        val query = _uiState.value.searchQuery
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(500)
                performSearch(resetResults = true)
            }
        }
    }

    private fun performSearch(resetResults: Boolean = false) {
        val query = _uiState.value.searchQuery.trim()

        if (query.isBlank()) return
        if (query.length < 2) return

        viewModelScope.launch {
            try {
                if (resetResults) {
                    _uiState.value = _uiState.value.copy(
                        searchState = SearchState.SEARCHING,
                        isSearching = true,
                        searchResults = emptyList(),
                        currentPage = 1,
                        hasMoreResults = true,
                        error = null
                    )
                }

                val page = if (resetResults) 1 else _uiState.value.currentPage
                when(val result = searchProductsUseCase(query, page)) {
                    is DomainResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            searchState = SearchState.ERROR,
                            isSearching = false,
                            isLoadingMore = false,
                            error = result.message
                        )
                    }
                    is DomainResult.Success -> {
                        val currentResults =
                            if (resetResults) emptyList() else _uiState.value.searchResults
                        val newResults = currentResults + result.data.results

                        _uiState.value = _uiState.value.copy(
                            searchResults = newResults,
                            searchState = if (newResults.isEmpty()) SearchState.EMPTY_RESULTS else SearchState.RESULTS,
                            isSearching = false,
                            isLoadingMore = false,
                            currentPage = page + 1,
                            hasMoreResults = result.data.hasMore,
                            error = null
                        )
                        saveRecentSearch(query)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchState = SearchState.ERROR,
                    isSearching = false,
                    isLoadingMore = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    private fun loadMoreResults() {
        val currentState = _uiState.value

        if (currentState.isLoadingMore ||
            !currentState.hasMoreResults ||
            currentState.searchQuery.isBlank()
        ) {
            return
        }

        _uiState.value = _uiState.value.copy(isLoadingMore = true)
        performSearch(resetResults = false)
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            try {
                when(val result = recentSearchesUseCase()) {
                    is DomainResult.Error -> {
                        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
                    }
                    is DomainResult.Success -> {
                        _uiState.value =
                            _uiState.value.copy(recentSearches = result.data.map { it.query })
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(recentSearches = emptyList())
            }
        }
    }

    private fun saveRecentSearch(query: String) {
        viewModelScope.launch {
            saveRecentSearchUseCase(query)
            loadRecentSearches()
        }
    }
}