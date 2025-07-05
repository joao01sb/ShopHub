package com.joao01sb.shophub.features.home.data.datasource

import com.joao01sb.shophub.core.data.remote.dto.PaginatedResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.data.remote.service.ApiService
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource

class ProductRemoteDataSourceImp(
    private val apiService: ApiService
) : ProductRemoteDataSource {
    override suspend fun getAllProducts(
        limit: Int,
        skip: Int
    ): PaginatedResponse<ProductDto> {
        return apiService.getAllProducts(limit, skip)
    }

    override suspend fun getProductById(id: Int): ProductDto  = apiService.getProductById(id)
}