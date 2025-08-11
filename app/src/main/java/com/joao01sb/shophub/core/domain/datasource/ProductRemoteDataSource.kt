package com.joao01sb.shophub.core.domain.datasource

import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.result.network.ApiResult

interface ProductRemoteDataSource {
    suspend fun getAllProducts(limit: Int, skip: Int): ApiResult<PaginatedProductsResponse<ProductDto>>
    suspend fun getProductById(id: Int): ApiResult<ProductDto>
    suspend fun searchProducts(query: String, skip: Int, limit: Int): ApiResult<PaginatedProductsResponse<ProductDto>>
}