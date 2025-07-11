package com.joao01sb.shophub.features.home.data.repository

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.datasource.RecentSearchDataSource
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class RecentSearchRepositoryImp(
    private val recentSearchDataSource: RecentSearchDataSource
) : RecentSearchRepository {

    override suspend fun insert(recentSearch: RecentSearchEntity) = recentSearchDataSource.insert(recentSearch)

    override suspend fun getRecentSearches(): List<RecentSearchEntity> = recentSearchDataSource.getRecentSearches()

}