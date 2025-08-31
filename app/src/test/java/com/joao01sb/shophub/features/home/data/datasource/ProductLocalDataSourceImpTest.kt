package com.joao01sb.shophub.features.home.data.datasource

import android.database.SQLException
import android.database.sqlite.SQLiteAbortException
import androidx.paging.PagingSource
import com.joao01sb.shophub.core.data.local.dao.ProductDao
import com.joao01sb.shophub.core.data.local.dao.RemoteKeysDao
import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.mapper.toEntity
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.result.database.DatabaseResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductLocalDataSourceImpTest {

    private lateinit var productDao: ProductDao
    private lateinit var remoteKeysDao: RemoteKeysDao
    private lateinit var productLocalDataSourceImp: ProductLocalDataSource

    @Before
    fun setUp() {
        clearAllMocks()
        productDao = mockk()
        remoteKeysDao = mockk()
        productLocalDataSourceImp = ProductLocalDataSourceImp(productDao, remoteKeysDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenGetAllProducts_whenGetAllProducts_thenCallDaoGetAllProducts() = runTest {
        val products = MockUtils.productsDto.map { it.toEntity() }
        val pagingSource = mockk<PagingSource<Int, ProductEntity>>()

        coEvery { pagingSource.load(any()) } answers {
            PagingSource.LoadResult.Page(
                data = products,
                prevKey = null,
                nextKey = null
            )
        }
        every { productDao.getAllProducts() } returns pagingSource
        val result = productLocalDataSourceImp.getAllProducts()
        val loadResult = result.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assert(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).data.size == products.size)

    }

    @Test
    fun givenGetAllProducts_whenGetAllProductsAndDaoThrowsException_thenThrowException() = runTest {
        val exception = SQLException("DB Error")

        every { productDao.getAllProducts() } throws exception
        try {
            productLocalDataSourceImp.getAllProducts()
            assert(false)
        } catch (e: Exception) {
            assert(e == exception)
        }
        verify { productDao.getAllProducts() }
    }

    @Test
    fun givenGetAllProducts_whenGetAllProductsEmpty_thenReturnEmptyPagingSource() = runTest {
        val products = emptyList<ProductEntity>()
        val pagingSource = mockk<PagingSource<Int, ProductEntity>>()

        coEvery { pagingSource.load(any()) } answers {
            PagingSource.LoadResult.Page(
                data = products,
                prevKey = null,
                nextKey = null
            )
        }
        every { productDao.getAllProducts() } returns pagingSource
        val result = productLocalDataSourceImp.getAllProducts()
        val loadResult = result.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assert(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).data.isEmpty())

    }

    @Test
    fun givenSearchProducts_whenSearchProducts_thenCallDaoSearchProducts() = runTest {
        val query = "Product 1"
        val products = MockUtils.productsDto.map { it.toEntity() }
        val resultFilter = products.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
        val pagingSource = mockk<PagingSource<Int, ProductEntity>>()

        coEvery { pagingSource.load(any()) } answers {
            PagingSource.LoadResult.Page(
                data = resultFilter,
                prevKey = null,
                nextKey = null
            )
        }
        every { productDao.searchProducts(query) } returns pagingSource
        val result = productLocalDataSourceImp.searchProducts(query)
        val loadResult = result.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assert(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).data == resultFilter)
        verify { productDao.searchProducts(query) }

    }

    @Test
    fun givenSearchProducts_whenSearchProductsNoMatch_thenReturnEmptyPagingSource() = runTest {
        val query = "Non-existent Product"
        val products = MockUtils.productsDto.map { it.toEntity() }
        val resultFilter = products.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)

        }
        val pagingSource = mockk<PagingSource<Int, ProductEntity>>()
        coEvery { pagingSource.load(any()) } answers {
            PagingSource.LoadResult.Page(
                data = resultFilter,
                prevKey = null,
                nextKey = null
            )
        }
        every { productDao.searchProducts(query) } returns pagingSource
        val result = productLocalDataSourceImp.searchProducts(query)
        val loadResult = result.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assert(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).data.isEmpty())
        verify { productDao.searchProducts(query) }
    }

    @Test
    fun givenSearchProducts_whenDaoThrowsException_thenThrowException() = runTest {
        val query = "Product 1"
        val exception = SQLiteAbortException("DB Error")

        every { productDao.searchProducts(query) } throws exception
        try {
            productLocalDataSourceImp.searchProducts(query)
            assert(false)
        } catch (e: Exception) {
            assert(e == exception)
        }
        verify { productDao.searchProducts(query) }
    }

    @Test
    fun givenInsertProducts_whenInsertProducts_thenCallDaoInsertProducts() = runTest {
        val products = MockUtils.productsDto.map { it.toEntity() }

        coEvery { productDao.insertProducts(products) } returns Unit
        val result = productLocalDataSourceImp.insertProducts(products)

        assert(result is DatabaseResult.Success)
        coVerify { productDao.insertProducts(products) }
    }

    @Test
    fun givenInsertProducts_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val products = MockUtils.productsDto.map { it.toEntity() }
        val exception = SQLiteAbortException("DB Error")

        coEvery { productDao.insertProducts(products) } throws exception
        val result = productLocalDataSourceImp.insertProducts(products)

        assert(result is DatabaseResult.UnknownError)
        coVerify { productDao.insertProducts(products) }
    }

    @Test
    fun givenGetProductById_whenProductExists_thenReturnProduct() = runTest {
        val product = MockUtils.productsDto.first().toEntity()
        val productId = product.id

        coEvery { productDao.getProductById(productId) } returns product
        val result = productLocalDataSourceImp.getProductById(productId)

        assert(result is DatabaseResult.Success)
        assert((result as DatabaseResult.Success).data == product)
        coVerify { productDao.getProductById(productId) }
    }

    @Test
    fun givenGetProductById_whenProductDoesNotExist_thenReturnNull() = runTest {
        val productId = 999

        coEvery { productDao.getProductById(productId) } returns null
        val result = productLocalDataSourceImp.getProductById(productId)

        assert(result is DatabaseResult.Success)
        assert((result as DatabaseResult.Success).data == null)
        coVerify { productDao.getProductById(productId) }
    }

    @Test
    fun givenGetProductById_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val productId = 1
        val exception = SQLiteAbortException("DB Error")

        coEvery { productDao.getProductById(productId) } throws exception
        val result = productLocalDataSourceImp.getProductById(productId)

        assert(result is DatabaseResult.UnknownError)
        coVerify { productDao.getProductById(productId) }
    }

    @Test
    fun givenClearProducts_whenCalled_thenCallDaoClearProducts() = runTest {
        coEvery { productDao.clearProducts() } returns Unit

        val result = productLocalDataSourceImp.clearProducts()

        assert(result is DatabaseResult.Success)
        coVerify { productDao.clearProducts() }
    }

    @Test
    fun givenClearProducts_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val exception = SQLiteAbortException("DB Error")

        coEvery { productDao.clearProducts() } throws exception
        val result = productLocalDataSourceImp.clearProducts()

        assert(result is DatabaseResult.UnknownError)
        coVerify { productDao.clearProducts() }
    }

    @Test
    fun givenGetRemoteKeyByProductId_whenRemoteKeyExists_thenReturnRemoteKey() = runTest {
        val remoteKey = MockUtils.remoteKeys.first()
        val productId = remoteKey.productId

        coEvery { remoteKeysDao.getRemoteKeyByProductId(productId) } returns remoteKey
        val result = productLocalDataSourceImp.getRemoteKeyByProductId(productId)

        assert(result is DatabaseResult.Success)
        assert((result as DatabaseResult.Success).data == remoteKey)
        coVerify { remoteKeysDao.getRemoteKeyByProductId(productId) }
    }

    @Test
    fun givenGetRemoteKeyByProductId_whenRemoteKeyDoesNotExist_thenReturnNull() = runTest {
        val productId = 999

        coEvery { remoteKeysDao.getRemoteKeyByProductId(productId) } returns null
        val result = productLocalDataSourceImp.getRemoteKeyByProductId(productId)

        assert(result is DatabaseResult.Success)
        assert((result as DatabaseResult.Success).data == null)
        coVerify { remoteKeysDao.getRemoteKeyByProductId(productId) }
    }

    @Test
    fun givenGetRemoteKeyByProductId_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val productId = 1
        val exception = SQLiteAbortException("DB Error")

        coEvery { remoteKeysDao.getRemoteKeyByProductId(productId) } throws exception
        val result = productLocalDataSourceImp.getRemoteKeyByProductId(productId)

        assert(result is DatabaseResult.UnknownError)
        coVerify { remoteKeysDao.getRemoteKeyByProductId(productId) }
    }

    @Test
    fun givenInsertRemoteKeys_whenInsertRemoteKeys_thenCallDaoInsertRemoteKeys() = runTest {
        val remoteKeys = MockUtils.remoteKeys

        coEvery { remoteKeysDao.insertRemoteKeys(remoteKeys) } returns Unit
        val result = productLocalDataSourceImp.insertRemoteKeys(remoteKeys)

        assert(result is DatabaseResult.Success)
        coVerify { remoteKeysDao.insertRemoteKeys(remoteKeys) }
    }

    @Test
    fun givenInsertRemoteKeys_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val remoteKeys = MockUtils.remoteKeys
        val exception = SQLiteAbortException("DB Error")

        coEvery { remoteKeysDao.insertRemoteKeys(remoteKeys) } throws exception
        val result = productLocalDataSourceImp.insertRemoteKeys(remoteKeys)

        assert(result is DatabaseResult.UnknownError)
        coVerify { remoteKeysDao.insertRemoteKeys(remoteKeys) }
    }

    @Test
    fun givenClearRemoteKeys_whenCalled_thenCallDaoClearRemoteKeys() = runTest {
        coEvery { remoteKeysDao.clearRemoteKeys() } returns Unit
        val result = productLocalDataSourceImp.clearRemoteKeys()

        assert(result is DatabaseResult.Success)
        coVerify { remoteKeysDao.clearRemoteKeys() }
    }

    @Test
    fun givenClearRemoteKeys_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val exception = SQLiteAbortException("DB Error")

        coEvery { remoteKeysDao.clearRemoteKeys() } throws exception
        val result = productLocalDataSourceImp.clearRemoteKeys()

        assert(result is DatabaseResult.UnknownError)
        coVerify { remoteKeysDao.clearRemoteKeys() }
    }

}