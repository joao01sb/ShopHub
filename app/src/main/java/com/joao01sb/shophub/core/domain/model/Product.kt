package com.joao01sb.shophub.core.domain.model

class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val category: String,
    val imageUrl: String?
)