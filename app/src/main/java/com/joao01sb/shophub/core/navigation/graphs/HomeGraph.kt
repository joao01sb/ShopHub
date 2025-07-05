package com.joao01sb.shophub.core.navigation.graphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.features.home.presentation.screen.HomeScreen
import com.joao01sb.shophub.features.home.presentation.viewmodel.HomeViewModel

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

                }
            )

        }
        composable<Routes.Details> {  }
        composable<Routes.Search> {  }
    }
}