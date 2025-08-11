package com.joao01sb.shophub.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joao01sb.shophub.core.data.local.entities.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query(
        "SELECT * FROM products " +
                "WHERE name " +
                "LIKE '%' || :query || '%' OR " +
                "description LIKE '%' || :query || '%' OR " +
                "category LIKE '%' || :query || '%'")
    fun searchProducts(query: String): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearProducts()
}