package com.joao01sb.shophub.core.data.mock

import com.joao01sb.shophub.core.domain.model.CartItem

object CartMocks {
    val sampleCartItems = listOf(
        CartItem(
            idProduto = 1,
            nome = "Camiseta Básica",
            quantidade = 2,
            precoUni = 29.90,
            precoTotal = 59.80,
            categoria = "Vestuário",
            imageUrl = "https://example.com/imagens/camiseta-basica.jpg"
        ),
        CartItem(
            idProduto = 2,
            nome = "Calça Jeans Slim",
            quantidade = 1,
            precoUni = 99.50,
            precoTotal = 99.50,
            categoria = "Vestuário",
            imageUrl = "https://example.com/imagens/calca-jeans.jpg"
        ),
        CartItem(
            idProduto = 3,
            nome = "Tênis Esportivo",
            quantidade = 1,
            precoUni = 180.00,
            precoTotal = 180.00,
            categoria = "Calçados",
            imageUrl = "https://example.com/imagens/tenis-esportivo.jpg"
        ),
        CartItem(
            idProduto = 4,
            nome = "Meia Soquete (3 pares)",
            quantidade = 1,
            precoUni = 15.00,
            precoTotal = 15.00,
            categoria = "Acessórios",
            imageUrl = "https://example.com/imagens/meia-soquete.jpg"
        ),
        CartItem(
            idProduto = 5,
            nome = "Jaqueta Corta Vento",
            quantidade = 1,
            precoUni = 149.90,
            precoTotal = 149.90,
            categoria = "Vestuário",
            imageUrl = "https://example.com/imagens/jaqueta-corta-vento.jpg"
        ),
        CartItem(
            idProduto = 6,
            nome = "Bermuda Cargo",
            quantidade = 2,
            precoUni = 75.00,
            precoTotal = 150.00,
            categoria = "Vestuário",
            imageUrl = "https://example.com/imagens/bermuda-cargo.jpg"
        ),
        CartItem(
            idProduto = 7,
            nome = "Óculos de Sol Aviador",
            quantidade = 1,
            precoUni = 120.00,
            precoTotal = 120.00,
            categoria = "Acessórios",
            imageUrl = "https://example.com/imagens/oculos-sol.jpg"
        ),
        CartItem(
            idProduto = 8,
            nome = "Relógio Digital",
            quantidade = 1,
            precoUni = 250.00,
            precoTotal = 250.00,
            categoria = "Acessórios",
            imageUrl = "https://example.com/imagens/relogio-digital.jpg"
        ),
        CartItem(
            idProduto = 9,
            nome = "Mochila Escolar",
            quantidade = 1,
            precoUni = 90.00,
            precoTotal = 90.00,
            categoria = "Acessórios",
            imageUrl = "https://example.com/imagens/mochila-escolar.jpg"
        ),
        CartItem(
            idProduto = 10,
            nome = "Boné Aba Reta",
            quantidade = 3,
            precoUni = 45.00,
            precoTotal = 135.00,
            categoria = "Acessórios",
            imageUrl = "https://example.com/imagens/bone-aba-reta.jpg"
        )
    )
}