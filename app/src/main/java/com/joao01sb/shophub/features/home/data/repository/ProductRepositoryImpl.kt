package com.joao01sb.shophub.features.home.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.joao01sb.shophub.core.data.local.ShopHubDatabase
import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.data.remote.ProductRemoteMediator
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productLocalDataSource: ProductLocalDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val database: ShopHubDatabase
) : ProductRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = ProductRemoteMediator(
                productRemoteDataSource,
                productLocalDataSource,
                database
            ),
            pagingSourceFactory = { productLocalDataSource.getAllProducts() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val localProduct = productLocalDataSource.getProductById(id)
            if (localProduct != null) {
                Result.success(localProduct.toDomain())
            } else {
                val remoteProduct = productRemoteDataSource.getProductById(id)
                Result.success(remoteProduct.toDomain())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchProducts(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { productLocalDataSource.searchProducts(query) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}