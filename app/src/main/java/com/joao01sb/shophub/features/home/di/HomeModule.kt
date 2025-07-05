package com.joao01sb.shophub.features.home.di

import com.joao01sb.shophub.core.data.local.ShopHubDatabase
import com.joao01sb.shophub.core.data.local.dao.ProductDao
import com.joao01sb.shophub.core.data.local.dao.RemoteKeysDao
import com.joao01sb.shophub.core.data.remote.service.ApiService
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.features.home.data.datasource.ProductLocalDataSourceImp
import com.joao01sb.shophub.features.home.data.datasource.ProductRemoteDataSourceImp
import com.joao01sb.shophub.features.home.data.repository.ProductRepositoryImpl
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import com.joao01sb.shophub.features.home.domain.usecase.GetAllproductsUseCase
import com.joao01sb.shophub.features.home.domain.usecase.GetProductByIdUseCase
import com.joao01sb.shophub.features.home.domain.usecase.SearchProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    fun provideProductLocalDatasource(
        productDao: ProductDao,
        remoteKeysDao: RemoteKeysDao
    ): ProductLocalDataSource {
        return ProductLocalDataSourceImp(productDao, remoteKeysDao)
    }

    @Provides
    fun provideProductRemoteDatasource(
        apiService: ApiService
    ): ProductRemoteDataSource {
        return ProductRemoteDataSourceImp(apiService)
    }

    @Provides
    fun provideProductRepository(
        productLocalDataSource: ProductLocalDataSource,
        productRemoteDataSource: ProductRemoteDataSource,
        database: ShopHubDatabase
    ): ProductRepository {
        return ProductRepositoryImpl(
            productLocalDataSource,
            productRemoteDataSource,
            database
        )
    }

    @Provides
    fun provideGetAllProductsUseCase(
        productRepository: ProductRepository
    ) : GetAllproductsUseCase {
        return GetAllproductsUseCase(productRepository)
    }

    @Provides
    fun provideGetProductByIdUseCase(
        productRepository: ProductRepository
    ) : GetProductByIdUseCase {
        return GetProductByIdUseCase(productRepository)
    }

    @Provides
    fun provideSearchProductUseCase(
        productRepository: ProductRepository
    ) : SearchProductsUseCase {
        return SearchProductsUseCase(productRepository)
    }

}