package com.joao01sb.shophub.features.home.data.datasource

import com.joao01sb.shophub.core.data.remote.dto.PaginatedResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.data.remote.service.ApiService
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.core.result.network.ApiResult
import com.joao01sb.shophub.core.result.network.safeApiCall

class ProductRemoteDataSourceImp(
    private val apiService: ApiService
) : ProductRemoteDataSource {

    override suspend fun getAllProducts(
        limit: Int,
        skip: Int
    ): ApiResult<PaginatedResponse<ProductDto>> = safeApiCall {
        apiService.getAllProducts(limit, skip)
    }

    override suspend fun getProductById(id: Int): ApiResult<ProductDto>  = safeApiCall {
        apiService.getProductById(id)
    }

    override suspend fun searchProducts(query: String, skip: Int, limit: Int): ApiResult<PaginatedResponse<ProductDto>> = safeApiCall {
        apiService.searchProducts(query, skip, limit)
    }
}