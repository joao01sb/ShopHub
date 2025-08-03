package com.joao01sb.shophub.core.domain.datasource

import com.joao01sb.shophub.core.data.remote.dto.PaginatedResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto

interface ProductRemoteDataSource {
    suspend fun getAllProducts(limit: Int, skip: Int): PaginatedResponse<ProductDto>
    suspend fun getProductById(id: Int): ProductDto
    suspend fun searchProducts(query: String, skip: Int, limit: Int): PaginatedResponse<ProductDto>
}