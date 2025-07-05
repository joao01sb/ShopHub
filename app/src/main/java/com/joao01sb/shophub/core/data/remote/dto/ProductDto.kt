package com.joao01sb.shophub.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    @SerialName("title")
    val nameInApi: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val category: String,
    val images: List<String>? = null,
    val brand: String? = null,
    val sku: String? = null,
)
