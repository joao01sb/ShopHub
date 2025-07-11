package com.joao01sb.shophub.core.domain.datasource

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity

interface RecentSearchDataSource {

    suspend fun insert(recentSearch: RecentSearchEntity)

    suspend fun getRecentSearches(): List<RecentSearchEntity>


}