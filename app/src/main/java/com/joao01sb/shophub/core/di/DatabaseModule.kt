package com.joao01sb.shophub.core.di

import android.content.Context
import androidx.room.Room
import com.joao01sb.shophub.core.data.local.ShopHubDatabase
import com.joao01sb.shophub.core.data.local.dao.ProductDao
import com.joao01sb.shophub.core.data.local.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideShopHubDatabase(@ApplicationContext context: Context): ShopHubDatabase {
        return Room.databaseBuilder(
            context,
            ShopHubDatabase::class.java,
            ShopHubDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideProductDao(database: ShopHubDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideRemoteKeysDao(database: ShopHubDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}