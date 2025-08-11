package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class ClearSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {

    suspend operator fun invoke(userId: String, query: String) : DomainResult<Unit> {
        return when (val result = recentSearchRepository.clearRecentSearches(userId, query)) {
            is DomainResult.Success -> result
            is DomainResult.Error -> result
        }
    }

}