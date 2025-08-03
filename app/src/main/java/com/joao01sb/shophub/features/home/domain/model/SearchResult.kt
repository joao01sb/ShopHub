package com.joao01sb.shophub.features.home.domain.model

import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.data.remote.dto.PaginatedResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.domain.model.Product

data class SearchResult(
    val results: List<Product>,
    val hasMore: Boolean
)

fun PaginatedResponse<ProductDto>.toSearchResult(): SearchResult {
    return SearchResult(
        this.products.map { it.toDomain() },
        skip + products.size < total
    )
}
