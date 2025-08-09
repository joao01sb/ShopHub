package com.joao01sb.shophub.core.navigation.graphs

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.features.home.presentation.event.DetailsEvent
import com.joao01sb.shophub.features.home.presentation.event.DetailsUiEvent
import com.joao01sb.shophub.features.home.presentation.event.SearchEvent
import com.joao01sb.shophub.features.home.presentation.screen.DetailsProductScreen
import com.joao01sb.shophub.features.home.presentation.screen.HomeScreen
import com.joao01sb.shophub.features.home.presentation.screen.SearchProductScreen
import com.joao01sb.shophub.features.home.presentation.viewmodel.HomeViewModel
import com.joao01sb.shophub.features.home.presentation.viewmodel.ProductDetailsViewModel
import com.joao01sb.shophub.features.home.presentation.viewmodel.SearchViewModel

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
                    navController.navigate(Routes.Search)
                },
                onCartClick = {
                    navController.navigate(Routes.Cart) {
                        launchSingleTop = true
                    }
                },
                onClickProduct = {
                    navController.navigate(Routes.Details(it))
                }
            )
        }

        composable<Routes.Details> {
            val viewModel = hiltViewModel<ProductDetailsViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val localContext = LocalContext.current

            LaunchedEffect(Unit) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is DetailsUiEvent.Success -> {
                            Toast.makeText(localContext, event.message, Toast.LENGTH_SHORT).show()
                        }
                        is DetailsUiEvent.NavigateToCart -> {
                            navController.navigate(Routes.Cart) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }

            DetailsProductScreen(
                uiState = state,
                onBack = {
                    navController.popBackStack()
                },
                onAddCart = {
                    viewModel.onEvent(DetailsEvent.AddToCart)
                },
                onNavigateToCart = {
                    viewModel.onEvent(DetailsEvent.NavigateToCard)
                }
            )
        }

        composable<Routes.Search> {
            val viewModel = hiltViewModel<SearchViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            SearchProductScreen(
                uiState = state,
                onNavigateToProduct = {
                    navController.navigate(Routes.Details(it.id))
                },
                onQueryChange = {
                    viewModel.onEvent(SearchEvent.QueryChanged(it))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onRetry = {
                    viewModel.onEvent(SearchEvent.Retry)
                },
                onRecentSearchClick = {
                    viewModel.onEvent(SearchEvent.RecentSearchClicked(it))
                },
                onLoadMore = {
                    viewModel.onEvent(SearchEvent.LoadMore)
                },
                onClearRecentSearches = {
                    viewModel.onEvent(SearchEvent.ClearRecentSearches(it))
                }
            )
        }
    }
}