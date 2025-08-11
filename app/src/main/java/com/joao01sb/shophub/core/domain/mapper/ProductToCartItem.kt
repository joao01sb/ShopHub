package com.joao01sb.shophub.core.domain.mapper

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.domain.model.Product

fun Product.toCartItem() = CartItem(
    productId = id,
    name = name,
    quantity = 1,
    uniPrice = price,
    totalPrice = price,
    category = category,
    urlImage = imageUrl ?: ""
)