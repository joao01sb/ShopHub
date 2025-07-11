package com.joao01sb.shophub.features.home.data.datasource

import com.joao01sb.shophub.core.data.local.dao.RecentSearchDao
import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.datasource.RecentSearchDataSource

class RecentSearchDataSourceImp(
    private val recentSearchDao: RecentSearchDao
) : RecentSearchDataSource {

    override suspend fun insert(recentSearch: RecentSearchEntity) =
        recentSearchDao.insert(recentSearch)

    override suspend fun getRecentSearches(): List<RecentSearchEntity> =
        recentSearchDao.getRecentSearches()

}