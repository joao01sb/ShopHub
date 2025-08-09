package com.joao01sb.shophub.core.domain.datasource

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.result.database.DatabaseResult

interface RecentSearchDataSource {

    suspend fun insert(recentSearch: RecentSearchEntity) : DatabaseResult<Unit>

    suspend fun getRecentSearches(userId: String): DatabaseResult<List<RecentSearchEntity>>

    suspend fun clearRecentSearches(userId: String, query: String) : DatabaseResult<Unit>

}