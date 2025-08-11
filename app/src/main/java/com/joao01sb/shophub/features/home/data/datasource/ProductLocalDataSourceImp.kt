package com.joao01sb.shophub.features.home.data.datasource

import androidx.paging.PagingSource
import com.joao01sb.shophub.core.data.local.dao.ProductDao
import com.joao01sb.shophub.core.data.local.dao.RemoteKeysDao
import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.local.entities.RemoteKeysEntity
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.result.database.DatabaseResult
import com.joao01sb.shophub.core.result.database.safeDatabaseCall

class ProductLocalDataSourceImp(
    private val productDao: ProductDao,
    private val remoteKeysDao: RemoteKeysDao
) : ProductLocalDataSource {

    override fun getAllProducts(): PagingSource<Int, ProductEntity> =
        productDao.getAllProducts()

    override fun searchProducts(query: String): PagingSource<Int, ProductEntity> =
        productDao.searchProducts(query)

    override suspend fun insertProducts(products: List<ProductEntity>): DatabaseResult<Unit> =
        safeDatabaseCall {
            productDao.insertProducts(products)
        }

    override suspend fun getProductById(id: Int): DatabaseResult<ProductEntity?> =
        safeDatabaseCall {
            productDao.getProductById(id)
        }

    override suspend fun clearProducts(): DatabaseResult<Unit> =
        safeDatabaseCall {
            productDao.clearProducts()
        }

    override suspend fun getRemoteKeyByProductId(productId: Int): DatabaseResult<RemoteKeysEntity?> =
        safeDatabaseCall {
            remoteKeysDao.getRemoteKeyByProductId(productId)
        }

    override suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeysEntity>): DatabaseResult<Unit> =
        safeDatabaseCall {
            remoteKeysDao.insertRemoteKeys(remoteKeys)
        }

    override suspend fun clearRemoteKeys(): DatabaseResult<Unit> =
        safeDatabaseCall {
            remoteKeysDao.clearRemoteKeys()
        }
}