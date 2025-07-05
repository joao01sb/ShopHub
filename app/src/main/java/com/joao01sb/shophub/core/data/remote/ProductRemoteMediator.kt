package com.joao01sb.shophub.core.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.joao01sb.shophub.core.data.local.ShopHubDatabase
import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.local.entities.RemoteKeysEntity
import com.joao01sb.shophub.core.data.mapper.toEntity
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
    private val database: ShopHubDatabase
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val skip = currentPage * state.config.pageSize
            val response = productRemoteDataSource.getAllProducts(
                limit = state.config.pageSize,
                skip = skip
            )

            val products = response.products
            val endOfPaginationReached = products.size < state.config.pageSize

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    productLocalDataSource.clearProducts()
                    productLocalDataSource.clearRemoteKeys()
                }

                val prevKey = if (currentPage > 0) currentPage - 1 else null
                val nextKey = if (endOfPaginationReached) null else currentPage + 1

                val remoteKeys = products.map { product ->
                    RemoteKeysEntity(
                        productId = product.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                productLocalDataSource.insertProducts(products.map { it.toEntity() })
                productLocalDataSource.insertRemoteKeys(remoteKeys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { productId ->
                productLocalDataSource.getRemoteKeyByProductId(productId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { product ->
                productLocalDataSource.getRemoteKeyByProductId(product.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { product ->
                productLocalDataSource.getRemoteKeyByProductId(product.id)
            }
    }
}