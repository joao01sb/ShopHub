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
import com.joao01sb.shophub.features.auth.presentation.event.AuthUiEvent
import com.joao01sb.shophub.features.cart.presentation.event.CartEvent
import com.joao01sb.shophub.features.cart.presentation.screen.CartScreen
import com.joao01sb.shophub.features.cart.presentation.state.CartUiEvent
import com.joao01sb.shophub.features.cart.presentation.viewmodel.CartViewModel

fun NavGraphBuilder.cartGraph(
    navController: NavHostController,
) {
    navigation<Routes.CartGraph>(
        startDestination = Routes.Cart
    ) {

        composable<Routes.Cart> {
            val viewModel = hiltViewModel<CartViewModel>()
            val uiState by viewModel.cartItems.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        CartUiEvent.Back -> {
                            navController.navigate(Routes.HomeGraph) {
                                popUpTo(Routes.AuthGraph) { inclusive = true }
                            }
                        }
                        CartUiEvent.Checkout -> {
                            navController.navigate(Routes.Checkout)
                        }
                    }
                }
            }

            CartScreen(
                uiState = uiState,
                onQuantityChange = { cartItem, quantity ->
                    viewModel.onEvent(CartEvent.UpdateCartItem(cartItem, quantity))
                },
                onRemoveItem = {
                    viewModel.onEvent(CartEvent.RemoveCartItem(it))
                },
                onCheckout = {
                    viewModel.onEvent(CartEvent.NavigateToCheckout)
                },
                onBack = {
                    viewModel.onEvent(CartEvent.Back)
                },
                onRetry = {
                    viewModel.onEvent(CartEvent.Retry)
                }
            )

        }
        composable<Routes.Checkout> {  }
    }
}