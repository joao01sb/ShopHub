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
            resultProducts(state, loadType)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun resultProducts(
        state: PagingState<Int, ProductEntity>,
        loadType: LoadType
    ): MediatorResult {
        val pageResult = getPageForLoadType(state, loadType)

        return if (pageResult.endPagination) {
            MediatorResult.Success(endOfPaginationReached = true)
        } else {
            loadProductsFromRemote(state, loadType, pageResult.page)
        }
    }

    private suspend fun getPageForLoadType(
        state: PagingState<Int, ProductEntity>,
        loadType: LoadType
    ): PageResult {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                val page = remoteKeys?.nextKey?.minus(1) ?: 0
                PageResult(page, false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    PageResult(0, remoteKeys != null)
                } else {
                    PageResult(prevKey, false)
                }
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    PageResult(0, remoteKeys != null)
                } else {
                    PageResult(nextKey, false)
                }
            }
        }
    }

    private suspend fun loadProductsFromRemote(
        state: PagingState<Int, ProductEntity>,
        loadType: LoadType,
        currentPage: Int
    ): MediatorResult {
        val skip = currentPage * state.config.pageSize

        return when (val response = productRemoteDataSource.getAllProducts(
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

            is ApiResult.NetworkError -> MediatorResult.Error(response.exception)
            is ApiResult.HttpError -> MediatorResult.Error(IOException("HTTP ${response.code}: ${response.message}"))
            is ApiResult.UnknownError -> MediatorResult.Error(response.exception)
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

    private data class PageResult(val page: Int, val endPagination: Boolean)
}