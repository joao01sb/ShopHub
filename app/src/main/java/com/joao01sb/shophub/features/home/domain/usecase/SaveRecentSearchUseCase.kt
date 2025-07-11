package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository

class SaveRecentSearchUseCase(
    private val repository: RecentSearchRepository
) {

    suspend operator fun invoke(query: String) {
        val recentSearch = RecentSearchEntity(query = query)
        repository.insert(recentSearch)
    }

}