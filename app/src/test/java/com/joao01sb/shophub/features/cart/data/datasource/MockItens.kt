package com.joao01sb.shophub.features.cart.data.datasource

import com.joao01sb.shophub.core.domain.model.CartItem

object MockItens {
    val mockCartItems = listOf(
        CartItem(
            productId = 1,
            name = "Smartphone Galaxy S21",
            quantity = 1,
            uniPrice = 3999.99,
            totalPrice = 3999.99,
            category = "Eletrônicos",
            urlImage = "https://example.com/galaxy-s21.jpg"
        ),
        CartItem(
            productId = 2,
            name = "Notebook Dell XPS",
            quantity = 1,
            uniPrice = 6999.99,
            totalPrice = 6999.99,
            category = "Computadores",
            urlImage = "https://example.com/dell-xps.jpg"
        ),
        CartItem(
            productId = 3,
            name = "Fone de Ouvido JBL",
            quantity = 2,
            uniPrice = 299.99,
            totalPrice = 599.98,
            category = "Áudio",
            urlImage = "https://example.com/jbl-phones.jpg"
        ),
        CartItem(
            productId = 4,
            name = "Smart TV 55\"",
            quantity = 1,
            uniPrice = 4299.99,
            totalPrice = 4299.99,
            category = "Eletrônicos",
            urlImage = "https://example.com/smart-tv.jpg"
        ),
        CartItem(
            productId = 5,
            name = "Mouse Gamer",
            quantity = 1,
            uniPrice = 199.99,
            totalPrice = 199.99,
            category = "Periféricos",
            urlImage = "https://example.com/mouse-gamer.jpg"
        ),
        CartItem(
            productId = 6,
            name = "Teclado Mecânico",
            quantity = 1,
            uniPrice = 399.99,
            totalPrice = 399.99,
            category = "Periféricos",
            urlImage = "https://example.com/teclado.jpg"
        ),
        CartItem(
            productId = 7,
            name = "Monitor 27\"",
            quantity = 2,
            uniPrice = 1499.99,
            totalPrice = 2999.98,
            category = "Monitores",
            urlImage = "https://example.com/monitor.jpg"
        ),
        CartItem(
            productId = 8,
            name = "Webcam HD",
            quantity = 1,
            uniPrice = 299.99,
            totalPrice = 299.99,
            category = "Periféricos",
            urlImage = "https://example.com/webcam.jpg"
        ),
        CartItem(
            productId = 9,
            name = "SSD 1TB",
            quantity = 1,
            uniPrice = 599.99,
            totalPrice = 599.99,
            category = "Armazenamento",
            urlImage = "https://example.com/ssd.jpg"
        ),
        CartItem(
            productId = 10,
            name = "Placa de Vídeo RTX 3060",
            quantity = 1,
            uniPrice = 2999.99,
            totalPrice = 2999.99,
            category = "Hardware",
            urlImage = "https://example.com/rtx3060.jpg"
        )
    )
}