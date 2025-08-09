package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class SaveRecentSearchUseCase(
    private val repository: RecentSearchRepository
) {

    suspend operator fun invoke(userId: String, query: String) : DomainResult<Unit> {
        val recentSearch = RecentSearchEntity(
            queryKey = query,
            userId = userId
        )
        return repository.insert(recentSearch)
    }

}