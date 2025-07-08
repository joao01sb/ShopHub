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
    @Serializable data object DetailsOrder : Routes()
}

fun Routes.route(): String {
    return when (this) {
        is Routes.AuthGraph -> "auth_graph"
        is Routes.Login -> "login"
        is Routes.Register -> "register"
        is Routes.CartGraph -> "cart_graph"
        is Routes.Cart -> "cart"
        is Routes.Checkout -> "checkout"
        is Routes.HomeGraph -> "home_graph"
        is Routes.Details -> "details"
        is Routes.Home -> "home"
        is Routes.Search -> "search"
        is Routes.OrdersGraph -> "orders_graph"
        is Routes.Orders -> "orders"
        is Routes.DetailsOrder -> "detailsorders"
    }
}