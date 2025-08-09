package com.joao01sb.shophub.features.home.data.datasource

import com.joao01sb.shophub.core.data.local.dao.RecentSearchDao
import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.datasource.RecentSearchDataSource
import com.joao01sb.shophub.core.result.database.DatabaseResult
import com.joao01sb.shophub.core.result.database.safeDatabaseCall

class RecentSearchDataSourceImp(
    private val recentSearchDao: RecentSearchDao
) : RecentSearchDataSource {

    override suspend fun insert(recentSearch: RecentSearchEntity) = safeDatabaseCall {
        recentSearchDao.insert(recentSearch)
    }

    override suspend fun clearRecentSearches(userId: String, query: String): DatabaseResult<Unit> = safeDatabaseCall {
        recentSearchDao.clearRecentSearches(userId, query)
    }

    override suspend fun getRecentSearches(userId: String): DatabaseResult<List<RecentSearchEntity>> = safeDatabaseCall {
        recentSearchDao.getRecentSearches(userId)
    }

}