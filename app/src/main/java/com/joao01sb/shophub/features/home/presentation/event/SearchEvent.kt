package com.joao01sb.shophub.features.home.presentation.event

sealed class SearchEvent {

    data class QueryChanged(val query: String) : SearchEvent()

    data object Search : SearchEvent()

}