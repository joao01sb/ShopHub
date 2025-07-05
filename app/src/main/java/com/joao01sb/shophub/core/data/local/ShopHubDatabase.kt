package com.joao01sb.shophub.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joao01sb.shophub.core.data.local.dao.ProductDao
import com.joao01sb.shophub.core.data.local.dao.RemoteKeysDao
import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.local.entities.RemoteKeysEntity

@Database(
    entities = [ProductEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShopHubDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "shophub_database"
    }
}