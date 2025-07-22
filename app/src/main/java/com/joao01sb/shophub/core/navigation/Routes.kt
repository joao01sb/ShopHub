package com.joao01sb.shophub.core.navigation

import kotlinx.serialization.Serializable


sealed class Routes {
    
    // Auth Feature Routes
    @Serializable data object AuthGraph : Routes()
    @Serializable data object Login : Routes()
    @Serializable data object Register : Routes()

    // Cart Feature
    @Serializable data object CartGraph : Routes()
    @Serializable data object Cart : Routes()
    @Serializable data object Checkout : Routes()

    // Home Feature
    @Serializable data object HomeGraph : Routes()
    @Serializable data object Home : Routes()
    @Serializable data class Details(val idUser: Int) : Routes()
    @Serializable data object Search : Routes()

    // Orders Feature
    @Serializable data object OrdersGraph : Routes()
    @Serializable data object Orders : Routes()
    @Serializable data class DetailsOrder(val orderId: String) : Routes()
}

fun Routes.route(): String {
    return when (this) {
        is Routes.AuthGraph -> "auth_graph"
        is Routes.Login -> "com.joao01sb.shophub.core.navigation.Routes.Login"
        is Routes.Register -> "com.joao01sb.shophub.core.navigation.Routes.Register"
        is Routes.CartGraph -> "cart_graph"
        is Routes.Cart -> "com.joao01sb.shophub.core.navigation.Routes.Cart"
        is Routes.Checkout -> "com.joao01sb.shophub.core.navigation.Routes.Checkout"
        is Routes.HomeGraph -> "home_graph"
        is Routes.Details -> "com.joao01sb.shophub.core.navigation.Routes.Details"
        is Routes.Home -> "com.joao01sb.shophub.core.navigation.Routes.Home"
        is Routes.Search -> "com.joao01sb.shophub.core.navigation.Routes.Search"
        is Routes.OrdersGraph -> "orders_graph"
        is Routes.Orders -> "com.joao01sb.shophub.core.navigation.Routes.Orders"
        is Routes.DetailsOrder -> "com.joao01sb.shophub.core.navigation.Routes.DetailsOrder"
    }
}