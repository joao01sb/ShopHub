package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(id: Int): DomainResult<Product> {
        return repository.getProductById(id)
    }

}