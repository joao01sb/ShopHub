package com.joao01sb.shophub.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedProductsResponse(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)