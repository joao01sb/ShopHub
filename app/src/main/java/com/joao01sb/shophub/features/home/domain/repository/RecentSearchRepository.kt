package com.joao01sb.shophub.features.home.domain.repository

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.result.DomainResult

interface RecentSearchRepository {

    suspend fun insert(recentSearch: RecentSearchEntity) : DomainResult<Unit>

    suspend fun getRecentSearches(userId: String): DomainResult<List<RecentSearchEntity>>

    suspend fun clearRecentSearches(userId: String, query: String) : DomainResult<Unit>

}