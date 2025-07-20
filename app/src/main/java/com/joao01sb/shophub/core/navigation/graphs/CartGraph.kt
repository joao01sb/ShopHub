package com.joao01sb.shophub.core.navigation.graphs

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.features.cart.presentation.event.CartEvent
import com.joao01sb.shophub.features.cart.presentation.event.CheckoutEvent
import com.joao01sb.shophub.features.cart.presentation.screen.CartScreen
import com.joao01sb.shophub.features.cart.presentation.screen.CheckoutScreen
import com.joao01sb.shophub.features.cart.presentation.state.CartUiEvent
import com.joao01sb.shophub.features.cart.presentation.state.CheckoutUiEvent
import com.joao01sb.shophub.features.cart.presentation.viewmodel.CartViewModel
import com.joao01sb.shophub.features.cart.presentation.viewmodel.CheckoutViewModel

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
                                popUpTo(Routes.CartGraph) { inclusive = true }
                            }
                        }
                        CartUiEvent.Checkout -> {
                            navController.navigate(Routes.Checkout) {
                                popUpTo(Routes.Cart) { inclusive = true }
                            }
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
        composable<Routes.Checkout> {
            val viewModel = hiltViewModel<CheckoutViewModel>()
            val uiState by viewModel.checkoutSate.collectAsStateWithLifecycle()
            val currentContex = LocalContext.current

            LaunchedEffect(Unit) {
                viewModel.checkoutUiEvent.collect { event ->
                    when (event) {
                        is CheckoutUiEvent.Back -> {
                            navController.navigate(Routes.Cart)
                        }
                        is CheckoutUiEvent.Error -> {
                            Toast.makeText(currentContex, event.message, Toast.LENGTH_LONG).show()
                        }
                        is CheckoutUiEvent.Finaly -> {
                            navController.navigate(Routes.HomeGraph) {
                                popUpTo(Routes.AuthGraph) { inclusive = true }
                            }
                        }
                    }
                }
            }

            CheckoutScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                onPlaceOrder = {
                    viewModel.onEvent(CheckoutEvent.Checkout)
                },
                onChangedCvv = {
                    viewModel.onEvent(CheckoutEvent.CardCVVChanged(it))
                },
                onChangedCardNumber = {
                    viewModel.onEvent(CheckoutEvent.CardNumberChanged(it))
                },
                onChangedExpirationCard = {
                    viewModel.onEvent(CheckoutEvent.CardValidationChanged(it))
                },
                onChangedCardHolderName = {
                    viewModel.onEvent(CheckoutEvent.CardNameChanged(it))
                },
                onChangedFullName = {
                    viewModel.onEvent(CheckoutEvent.FullNameChanged(it))
                },
                onChangedPhone = {
                    viewModel.onEvent(CheckoutEvent.PhoneChanged(it))
                },
                onBack = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(Routes.Checkout) { inclusive = true }
                    }
                }
            )
        }
    }
}