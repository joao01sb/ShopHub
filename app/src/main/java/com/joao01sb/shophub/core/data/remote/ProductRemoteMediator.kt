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
import com.joao01sb.shophub.core.result.network.ApiResult
import com.joao01sb.shophub.core.result.database.DatabaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

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

            when (val response = productRemoteDataSource.getAllProducts(
                limit = state.config.pageSize,
                skip = skip
            )) {
                is ApiResult.Success -> {
                    val products = response.data.products
                    val endOfPaginationReached = products.size < state.config.pageSize

                    withContext(Dispatchers.IO) {
                        database.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                database.remoteKeysDao().clearRemoteKeys()
                                database.productDao().clearProducts()
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

                            database.productDao().insertProducts(products.map { it.toEntity() })
                            database.remoteKeysDao().insertRemoteKeys(remoteKeys)
                        }
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is ApiResult.NetworkError -> {
                    MediatorResult.Error(response.exception)
                }

                is ApiResult.HttpError -> {
                    MediatorResult.Error(IOException("HTTP ${response.code}: ${response.message}"))
                }

                is ApiResult.UnknownError -> {
                    MediatorResult.Error(response.exception)
                }
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { productId ->
                when (val result = productLocalDataSource.getRemoteKeyByProductId(productId)) {
                    is DatabaseResult.Success -> result.data
                    else -> null
                }
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { product ->
                when (val result = productLocalDataSource.getRemoteKeyByProductId(product.id)) {
                    is DatabaseResult.Success -> result.data
                    else -> null
                }
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { product ->
                when (val result = productLocalDataSource.getRemoteKeyByProductId(product.id)) {
                    is DatabaseResult.Success -> result.data
                    else -> null
                }
            }
    }
}