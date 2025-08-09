package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class GetRecentSearchesUseCase(
    private val repository: RecentSearchRepository
) {

    suspend operator fun invoke(userId: String) : DomainResult<List<RecentSearchEntity>> {
        return when(val result = repository.getRecentSearches(userId)) {
            is DomainResult.Success -> DomainResult.Success(result.data)
            is DomainResult.Error -> result
        }
    }

}