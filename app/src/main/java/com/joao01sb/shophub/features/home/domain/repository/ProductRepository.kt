package com.joao01sb.shophub.features.home.domain.repository

import androidx.paging.PagingData
import com.joao01sb.shophub.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getAllProducts(): Flow<PagingData<Product>>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun searchProducts(query: String): Flow<PagingData<Product>>

}