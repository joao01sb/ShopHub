package com.joao01sb.shophub.core.data.remote.service

import com.joao01sb.shophub.core.data.remote.dto.PaginatedResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/products")
    suspend fun getAllProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): PaginatedResponse<ProductDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): ProductDto

}