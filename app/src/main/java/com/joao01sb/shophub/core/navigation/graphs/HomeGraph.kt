package com.joao01sb.shophub.core.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.features.home.presentation.screen.DetailsProductScreen
import com.joao01sb.shophub.features.home.presentation.screen.HomeScreen
import com.joao01sb.shophub.features.home.presentation.viewmodel.HomeViewModel
import com.joao01sb.shophub.features.home.presentation.viewmodel.ProductDetailsViewModel

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation<Routes.HomeGraph>(
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val products = viewModel.products.collectAsLazyPagingItems()
            HomeScreen(
                products = products,
                onSearchClick = {

                },
                onCartClick = {

                },
                onClickProduct = {
                    navController.navigate(Routes.Details(it))
                }
            )

        }
        composable<Routes.Details> {
            val viewModel = hiltViewModel<ProductDetailsViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            DetailsProductScreen(
                uiState = state,
                onBack = {
                    navController.popBackStack()
                },
                onAddCart = {

                }
            )
        }
        composable<Routes.Search> {  }
    }
}