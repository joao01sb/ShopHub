package com.joao01sb.shophub.features.home.data.repository

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.datasource.RecentSearchDataSource
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.database.DatabaseResult
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class RecentSearchRepositoryImp(
    private val recentSearchDataSource: RecentSearchDataSource
) : RecentSearchRepository {

    override suspend fun insert(recentSearch: RecentSearchEntity) : DomainResult<Unit> {
        return when(val result = recentSearchDataSource.insert(recentSearch)) {
            is DatabaseResult.DatabaseError -> DomainResult.Error(result.exception.message?: "Unknown error", ErrorType.DATABASE)
            is DatabaseResult.Success<*> -> DomainResult.Success(Unit)
            is DatabaseResult.UnknownError -> DomainResult.Error(result.exception.message?: "Unknown error", ErrorType.UNKNOWN)
        }
    }

    override suspend fun getRecentSearches(): DomainResult<List<RecentSearchEntity>> {
        return when(val result = recentSearchDataSource.getRecentSearches()) {
            is DatabaseResult.DatabaseError -> DomainResult.Error(result.exception.message?: "Unknown error", ErrorType.DATABASE)
            is DatabaseResult.Success<*> -> {
                val data = result.data as List<RecentSearchEntity>
                DomainResult.Success(data)
            }
            is DatabaseResult.UnknownError -> DomainResult.Error(result.exception.message?: "Unknown error", ErrorType.UNKNOWN)
        }
    }

}