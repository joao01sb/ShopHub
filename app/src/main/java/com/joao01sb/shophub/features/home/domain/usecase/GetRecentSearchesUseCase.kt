package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class GetRecentSearchesUseCase(
    private val repository: RecentSearchRepository
) {

    suspend operator fun invoke() : Result<List<RecentSearchEntity>> {
        return try {
            val recentSearches = repository.getRecentSearches()
            Result.success(recentSearches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}