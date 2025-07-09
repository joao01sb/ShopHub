package com.joao01sb.shophub.features.home.presentation.event

sealed interface SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent
    data class RecentSearchClicked(val search: String) : SearchEvent
    object Search : SearchEvent
    object Retry : SearchEvent
    object ClearError : SearchEvent
}
