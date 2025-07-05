package com.joao01sb.shophub.features.home.domain.usecase

import androidx.paging.PagingData
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetAllproductsUseCase(
    private val repository: ProductRepository
) {

    operator fun invoke(): Flow<PagingData<Product>> {
        return repository.getAllProducts()
    }

}