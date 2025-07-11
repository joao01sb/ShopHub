package com.joao01sb.shophub.features.home.domain.repository

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity

interface RecentSearchRepository {

    suspend fun insert(recentSearch: RecentSearchEntity)

    suspend fun getRecentSearches(): List<RecentSearchEntity>

}