package com.joao01sb.shophub.core.domain.model

class RecentSearch(
    val query: String,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)