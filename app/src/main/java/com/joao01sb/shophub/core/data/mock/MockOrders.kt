package com.joao01sb.shophub.core.data.mock

import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

object MockOrders {
    val orders = listOf(
        Order(
            id = "1",
            items = listOf(
                CartItem(1, "Camiseta Branca", 2, 49.9, 99.8, "Roupas", "url1"),
                CartItem(2, "Tênis Esportivo", 1, 199.9, 199.9, "Calçados", "url2")
            ),
            total = 299.7,
            status = OrderStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "1234 5678 9012 3456",
                "João Silva",
                "12/25",
                "123",
                "João Silva",
                "11999999999"
            )
        ),
        Order(
            id = "2",
            items = listOf(
                CartItem(3, "Fone de Ouvido", 1, 89.9, 89.9, "Eletrônicos", "url3")
            ),
            total = 89.9,
            status = OrderStatus.PENDING,
            createdAt = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "5678 9012 3456 7890",
                "Maria Santos",
                "11/24",
                "456",
                "Maria Santos",
                "11988888888"
            )
        ),
        Order(
            id = "3",
            items = listOf(
                CartItem(4, "Fone Bluetooth", 1, 89.9, 89.9, "Eletrônicos", "url4")
            ),
            total = 89.9,
            status = OrderStatus.PROCESSING,
            createdAt = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "5555 6666 7777 8888",
                "Carlos Lima",
                "10/25",
                "456",
                "Carlos Lima",
                "11977777777"
            )
        ),
        Order(
            id = "4",
            items = listOf(
                CartItem(5, "Relógio Digital", 1, 159.9, 159.9, "Acessórios", "url5")
            ),
            total = 159.9,
            status = OrderStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "9999 8888 7777 6666",
                "Ana Paula",
                "09/28",
                "789",
                "Ana Paula",
                "11966666666"
            )
        ),
        Order(
            id = "5",
            items = listOf(
                CartItem(6, "Mochila Escolar", 1, 79.9, 79.9, "Acessórios", "url6")
            ),
            total = 79.9,
            status = OrderStatus.PENDING,
            createdAt = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "1111 2222 3333 4444",
                "Pedro Alves",
                "08/27",
                "147",
                "Pedro Alves",
                "11955555555"
            )
        ),
        Order(
            id = "6",
            items = listOf(
                CartItem(7, "Livro Kotlin", 2, 59.9, 119.8, "Livros", "url7")
            ),
            total = 119.8,
            status = OrderStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "2222 3333 4444 5555",
                "Lucas Dias",
                "07/26",
                "258",
                "Lucas Dias",
                "11944444444"
            )
        ),
        Order(
            id = "7",
            items = listOf(
                CartItem(8, "Mouse Gamer", 1, 99.9, 99.9, "Eletrônicos", "url8")
            ),
            total = 99.9,
            status = OrderStatus.PROCESSING,
            createdAt = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000),
            paymentInfo = CheckoutInfo(
                "3333 4444 5555 6666",
                "Fernanda Reis",
                "06/25",
                "369",
                "Fernanda Reis",
                "11933333333"
            )
        ),
        Order(
            id = "8",
            items = listOf(
                CartItem(9, "Copo Térmico", 3, 29.9, 89.7, "Casa", "url9")
            ),
            total = 89.7,
            status = OrderStatus.COMPLETED,
            createdAt = System.currentTimeMillis(),
            paymentInfo = CheckoutInfo(
                "4444 5555 6666 7777",
                "Gabriel Costa",
                "05/28",
                "147",
                "Gabriel Costa",
                "11922222222"
            )
        )
    )
}