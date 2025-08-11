package com.joao01sb.shophub.features.home.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.joao01sb.shophub.core.data.local.ShopHubDatabase
import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.data.remote.ProductRemoteMediator
import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.DomainResult.Error
import com.joao01sb.shophub.core.result.DomainResult.Success
import com.joao01sb.shophub.core.result.database.DatabaseResult
import com.joao01sb.shophub.core.result.network.ApiResult
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

    override suspend fun getProductById(id: Int): DomainResult<Product> {
        val localResult = productLocalDataSource.getProductById(id)

        if (localResult is DatabaseResult.Success && localResult.data != null) {
            return Success(localResult.data.toDomain())
        }

        return handleRemoteProductResult(productRemoteDataSource.getProductById(id))
    }

    private fun handleRemoteProductResult(remoteResult: ApiResult<ProductDto>): DomainResult<Product> {
        return when (remoteResult) {
            is ApiResult.HttpError ->
                DomainResult.Error(remoteResult.message, ErrorType.NETWORK)

            is ApiResult.NetworkError ->
                DomainResult.Error(
                    remoteResult.exception.message ?: "Unknown error",
                    ErrorType.NETWORK
                )

            is ApiResult.Success ->
                Success(remoteResult.data.toDomain())

            is ApiResult.UnknownError ->
                DomainResult.Error(
                    remoteResult.exception.message ?: "Unknown error",
                    ErrorType.NETWORK
                )
        }
    }

    override suspend fun searchProducts(
        query: String,
        page: Int,
        limit: Int
    ): DomainResult<PaginatedProductsResponse<ProductDto>> {
        val skip = (page - 1) * limit
        return when (val remoteResult =
            productRemoteDataSource.searchProducts(query, skip, limit)) {
            is ApiResult.HttpError ->
                Error(remoteResult.message, ErrorType.NETWORK)

            is ApiResult.NetworkError ->
                Error(remoteResult.exception.message ?: "Unknown error", ErrorType.NETWORK)

            is ApiResult.Success -> {
                val remoteProducts = remoteResult.data
                Success(remoteProducts)
            }

            is ApiResult.UnknownError ->
                Error(remoteResult.exception.message ?: "Unknown error", ErrorType.NETWORK)
        }
    }
}