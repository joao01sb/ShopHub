package com.joao01sb.shophub.features.orders.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.features.orders.data.datasource.RemoteOrdersDataSourceImp
import com.joao01sb.shophub.features.orders.data.repository.OrdersRepositoryImp
import com.joao01sb.shophub.features.orders.domain.datasource.RemoteOrdersDataSource
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository
import com.joao01sb.shophub.features.orders.domain.usecase.GetOrdersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object OrdersModule {

    @Provides
    fun provideRemoteOrdersDataSource(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : RemoteOrdersDataSource {
        return RemoteOrdersDataSourceImp(
            firestore,
            firebaseAuth
        )
    }

    @Provides
    fun provideOrderRepository(
        dataSource: RemoteOrdersDataSource
    ): OrdersRepository {
        return OrdersRepositoryImp(dataSource)
    }

    @Provides
    fun provideGetOrdersUseCase(
        repository: OrdersRepository
    ): GetOrdersUseCase {
        return GetOrdersUseCase(repository)
    }

}