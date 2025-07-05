package com.joao01sb.shophub.core.data.mapper

import com.joao01sb.shophub.core.data.local.entities.ProductEntity
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.domain.model.Product

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = nameInApi,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        category = category,
        imageUrl = images?.firstOrNull()
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        category = category,
        imageUrl = imageUrl
    )
}

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = nameInApi,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        category = category,
        imageUrl = images?.firstOrNull()
    )
}