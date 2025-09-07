package com.joao01sb.shophub.features.orders.data.datasource

import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import java.util.UUID

object MockUtils {

    val cartItems = listOf<CartItem>(
        CartItem(
            productId = 1,
            name = "Product 1",
            quantity = 1,
            uniPrice = 1.0,
            totalPrice = 1.0,
            category = "Category 1",
            urlImage = "image_url"
        ),
        CartItem(
            productId = 2,
            name = "Product 2",
            quantity = 2,
            uniPrice = 2.0,
            totalPrice = 4.0,
            category = "Category 2",
            urlImage = "image_url"
        ),
        CartItem(
            productId = 3,
            name = "Product 3",
            quantity = 3,
            uniPrice = 3.0,
            totalPrice = 9.0,
            category = "Category 3",
            urlImage = "image_url"
        )
    )

    val cartItems2 = listOf<CartItem>(
        CartItem(
            productId = 4,
            name = "Product 4",
            quantity = 4,
            uniPrice = 4.0,
            totalPrice = 16.0,
            category = "Category 4",
            urlImage = "image_url"
        ),
        CartItem(
            productId = 5,
            name = "Product 5",
            quantity = 5,
            uniPrice = 5.0,
            totalPrice = 25.0,
            category = "Category 5",
            urlImage = "image_url"
        ),
        CartItem(
            productId = 6,
            name = "Product 6",
            quantity = 6,
            uniPrice = 6.0,
            totalPrice = 36.0,
            category = "Category 6",
            urlImage = "image_url"
        )
    )

    val paymentInfo = CheckoutInfo(
        numberCard = "1234567890123456",
        nameCard = "Joao oie",
        dateCard = "12/25",
        cvvCard = "123",
        fullName = "Joao Carlos",
        phoneNumber = "11954480000"
    )

    val orders = listOf(
        Order(
            id = UUID.randomUUID().toString(),
            items = cartItems,
            total = cartItems.sumOf { it.totalPrice },
            status = OrderStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            paymentInfo = paymentInfo,
            orderNumber = "1"
        ),
        Order(
            id = UUID.randomUUID().toString(),
            items = cartItems2,
            total = cartItems2.sumOf { it.totalPrice },
            status = OrderStatus.COMPLETED,
            createdAt = System.currentTimeMillis(),
            paymentInfo = paymentInfo,
            orderNumber = "2"
        )
    )

}