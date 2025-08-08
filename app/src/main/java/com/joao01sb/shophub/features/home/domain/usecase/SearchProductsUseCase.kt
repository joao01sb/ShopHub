package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.model.SearchResult
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import com.joao01sb.shophub.features.home.domain.model.toSearchResult

class SearchProductsUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(query: String, page: Int = 1): DomainResult<SearchResult> {
        return when(val result = repository.searchProducts(query, page)) {
            is DomainResult.Success -> {
                val searchResult = result.data.toSearchResult()
                DomainResult.Success(searchResult)
            }
            is DomainResult.Error -> result
        }
    }

}