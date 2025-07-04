package com.joao01sb.shophub.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.joao01sb.shophub.core.navigation.Routes

fun NavGraphBuilder.ordersGraph(
    navController: NavHostController
) {
    navigation<Routes.OrdersGraph>(
        startDestination = Routes.Orders
    ) {
        composable<Routes.Orders> {  }
        composable<Routes.DetailsOrder> {  }
    }
}