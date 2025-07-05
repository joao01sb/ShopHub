package com.joao01sb.shophub.core.data.remote

import androidx.compose.ui.graphics.Path
import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/products")
    suspend fun getAllProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): PaginatedProductsResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): ProductDto

}