package com.joao01sb.shophub.core.domain.datasource

import androidx.paging.PagingSource
import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.local.entities.RemoteKeysEntity
import com.joao01sb.shophub.core.result.database.DatabaseResult

interface ProductLocalDataSource {
    fun getAllProducts(): PagingSource<Int, ProductEntity>

    fun searchProducts(query: String): PagingSource<Int, ProductEntity>

    suspend fun insertProducts(products: List<ProductEntity>) : DatabaseResult<Unit>

    suspend fun getProductById(id: Int): DatabaseResult<ProductEntity?>

    suspend fun clearProducts() : DatabaseResult<Unit>

    suspend fun getRemoteKeyByProductId(productId: Int): DatabaseResult<RemoteKeysEntity?>

    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeysEntity>) : DatabaseResult<Unit>

    suspend fun clearRemoteKeys() : DatabaseResult<Unit>
}