package com.joao01sb.shophub.core.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.features.orders.presentation.event.OrderUiEvent
import com.joao01sb.shophub.features.orders.presentation.screen.OrderDetailsScreen
import com.joao01sb.shophub.features.orders.presentation.screen.OrdersScreen
import com.joao01sb.shophub.features.orders.presentation.state.DetailsOrderEvent
import com.joao01sb.shophub.features.orders.presentation.state.OrdersEvent
import com.joao01sb.shophub.features.orders.presentation.viewmodel.DetailsOrderViewModel
import com.joao01sb.shophub.features.orders.presentation.viewmodel.OrdersViewModel

fun NavGraphBuilder.ordersGraph(
    navController: NavHostController
) {
    navigation<Routes.OrdersGraph>(
        startDestination = Routes.Orders
    ) {
        composable<Routes.Orders> {
            val viewModel = hiltViewModel<OrdersViewModel>()
            val ordersState by viewModel.ordersUiState.collectAsStateWithLifecycle()
            LaunchedEffect(Unit) {
                viewModel.orderUiEvent.collect { event ->
                    when (event) {
                        is OrderUiEvent.Logout -> {
                            navController.navigate(Routes.AuthGraph) {
                                popUpTo(Routes.OrdersGraph) { inclusive = true }
                            }
                        }
                    }
                }
            }
            OrdersScreen(
                orderUiState = ordersState,
                onOrderClick = { orderId ->
                    navController.navigate(Routes.DetailsOrder(orderId.id))
                },
                onBackClick = {
                    navController.navigate(Routes.HomeGraph) {
                        popUpTo(Routes.OrdersGraph) { inclusive = true }
                    }
                },
                onRetry = {
                    viewModel.onEvent(OrdersEvent.RefreshOrders)
                },
                onLogoutClick = {
                    viewModel.onEvent(OrdersEvent.Logout)
                }
            )
        }

        composable<Routes.DetailsOrder> {
            val viewModel = hiltViewModel<DetailsOrderViewModel>()
            val orderDetailsState by viewModel.orderDetailsUiState.collectAsStateWithLifecycle()
            OrderDetailsScreen(
                orderDetalsState = orderDetailsState,
                onBackClick = {
                    navController.navigate(Routes.Orders)
                },
                onRetry = {
                    viewModel.onEvent(DetailsOrderEvent.RefreshOrder)
                }
            )
        }
    }
}