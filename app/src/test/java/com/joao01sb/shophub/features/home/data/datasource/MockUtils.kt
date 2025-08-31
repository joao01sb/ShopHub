package com.joao01sb.shophub.features.home.data.datasource

import com.joao01sb.shophub.core.data.local.entities.RemoteKeysEntity
import com.joao01sb.shophub.core.data.remote.dto.ProductDto

object MockUtils {

    val productsDto = listOf<ProductDto>(
        ProductDto(
            id = 1,
            nameInApi = "Product 1",
            description = "Description 1",
            price = 100.0,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 10,
            category = "Category 1",
            images = listOf("https://example.com/image1_1.jpg", "https://example.com/image1_2.jpg"),
            brand = "Brand 1",
            sku = null
        ),
        ProductDto(
            id = 2,
            nameInApi = "Product 2",
            description = "Description 2",
            price = 200.0,
            discountPercentage = 20.0,
            rating = 4.0,
            stock = 20,
            category = "Category 2",
            images = listOf("https://example.com/image2_1.jpg", "https://example.com/image2_2.jpg"),
            brand = "Brand 2",
            sku = null
        ),
        ProductDto(
            id = 3,
            nameInApi = "Product 3",
            description = "Description 3",
            price = 300.0,
            discountPercentage = 30.0,
            rating = 3.5,
            stock = 30,
            category = "Category 3",
            images = listOf("https://example.com/image3_1.jpg", "https://example.com/image3_2.jpg"),
            brand = "Brand 3",
            sku = null
        )
    )

    val remoteKeys = listOf<RemoteKeysEntity>(
        RemoteKeysEntity(
            productId = 1,
            prevKey = null,
            nextKey = 2
        ),
        RemoteKeysEntity(
            productId = 2,
            prevKey = 1,
            nextKey = 3
        ),
        RemoteKeysEntity(
            productId = 3,
            prevKey = 2,
            nextKey = null
        )
    )

}