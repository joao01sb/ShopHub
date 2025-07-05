package com.joao01sb.shophub.core.domain.datasource

import androidx.paging.PagingSource
import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.local.entities.RemoteKeysEntity

interface ProductLocalDataSource {
    fun getAllProducts(): PagingSource<Int, ProductEntity>

    fun searchProducts(query: String): PagingSource<Int, ProductEntity>

    suspend fun insertProducts(products: List<ProductEntity>)

    suspend fun getProductById(id: Int): ProductEntity?

    suspend fun clearProducts()

    suspend fun getRemoteKeyByProductId(productId: Int): RemoteKeysEntity?

    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeysEntity>)

    suspend fun clearRemoteKeys()
}