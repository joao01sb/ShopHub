package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.features.home.domain.model.SearchResult
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import com.joao01sb.shophub.features.home.domain.model.toSearchResult

class SearchProductsUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(query: String, page: Int = 1): Result<SearchResult> {
        return try {
            val response = repository.searchProducts(query, page)
            if (response.isSuccess) {
                Result.success(response.getOrThrow().toSearchResult())
            } else {
                Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}