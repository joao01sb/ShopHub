package com.joao01sb.shophub.core.data.mock

import com.joao01sb.shophub.core.domain.model.CartItem

object CartMocks {
    val sampleCartItems = listOf(
        CartItem(
            productId = 1,
            name = "Camiseta Básica",
            quantity = 2,
            uniPrice = 29.90,
            totalPrice = 59.80,
            category = "Vestuário",
            urlImage = "https://example.com/imagens/camiseta-basica.jpg"
        ),
        CartItem(
            productId = 2,
            name = "Calça Jeans Slim",
            quantity = 1,
            uniPrice = 99.50,
            totalPrice = 99.50,
            category = "Vestuário",
            urlImage = "https://example.com/imagens/calca-jeans.jpg"
        ),
        CartItem(
            productId = 3,
            name = "Tênis Esportivo",
            quantity = 1,
            uniPrice = 180.00,
            totalPrice = 180.00,
            category = "Calçados",
            urlImage = "https://example.com/imagens/tenis-esportivo.jpg"
        ),
        CartItem(
            productId = 4,
            name = "Meia Soquete (3 pares)",
            quantity = 1,
            uniPrice = 15.00,
            totalPrice = 15.00,
            category = "Acessórios",
            urlImage = "https://example.com/imagens/meia-soquete.jpg"
        ),
        CartItem(
            productId = 5,
            name = "Jaqueta Corta Vento",
            quantity = 1,
            uniPrice = 149.90,
            totalPrice = 149.90,
            category = "Vestuário",
            urlImage = "https://example.com/imagens/jaqueta-corta-vento.jpg"
        ),
        CartItem(
            productId = 6,
            name = "Bermuda Cargo",
            quantity = 2,
            uniPrice = 75.00,
            totalPrice = 150.00,
            category = "Vestuário",
            urlImage = "https://example.com/imagens/bermuda-cargo.jpg"
        ),
        CartItem(
            productId = 7,
            name = "Óculos de Sol Aviador",
            quantity = 1,
            uniPrice = 120.00,
            totalPrice = 120.00,
            category = "Acessórios",
            urlImage = "https://example.com/imagens/oculos-sol.jpg"
        ),
        CartItem(
            productId = 8,
            name = "Relógio Digital",
            quantity = 1,
            uniPrice = 250.00,
            totalPrice = 250.00,
            category = "Acessórios",
            urlImage = "https://example.com/imagens/relogio-digital.jpg"
        ),
        CartItem(
            productId = 9,
            name = "Mochila Escolar",
            quantity = 1,
            uniPrice = 90.00,
            totalPrice = 90.00,
            category = "Acessórios",
            urlImage = "https://example.com/imagens/mochila-escolar.jpg"
        ),
        CartItem(
            productId = 10,
            name = "Boné Aba Reta",
            quantity = 3,
            uniPrice = 45.00,
            totalPrice = 135.00,
            category = "Acessórios",
            urlImage = "https://example.com/imagens/bone-aba-reta.jpg"
        )
    )
}