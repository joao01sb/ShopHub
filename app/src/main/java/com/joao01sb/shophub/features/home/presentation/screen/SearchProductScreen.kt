package com.joao01sb.shophub.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.enum.SearchState
import com.joao01sb.shophub.features.home.presentation.state.SearchUiState
import com.joao01sb.shophub.shared_ui.components.ProductCard
import com.joao01sb.shophub.shared_ui.components.RecentSearchesContent
import com.joao01sb.shophub.shared_ui.components.SearchProductBar

@Composable
fun SearchProductScreen(
    uiState: SearchUiState,
    products: LazyPagingItems<Product>,
    onNavigateToProduct: (Product) -> Unit,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onHasResults: (Boolean) -> Unit,
) {

    LaunchedEffect(products.loadState) {
        when (products.loadState.refresh) {
            is LoadState.NotLoading -> {
                if (products.itemCount > 0) {
                    onHasResults(true)
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchProductBar(
            query = uiState.searchQuery,
            onQueryChange = onQueryChange,
            placeholder = "Search products...",
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState.searchState) {
            SearchState.RECENT_SEARCHES -> {
                RecentSearchesContent(
                    recentSearches = uiState.recentSearches,
                    onRecentSearchClick = onRecentSearchClick
                )
            }

            SearchState.SEARCHING -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            SearchState.RESULTS -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products.itemCount) { index ->
                        val product = products[index]
                        if (product != null) {
                            ProductCard(
                                product = product,
                                onClick = { onNavigateToProduct(product) }
                            )
                        }
                    }

                    when (products.loadState.refresh) {
                        is LoadState.Loading -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = (products.loadState.refresh as LoadState.Error).error.message ?: "Unknow error")
                                    Button(
                                        onClick = { products.retry() }
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                        else -> {}
                    }

                    when (products.loadState.append) {
                        is LoadState.Loading -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                Button(
                                    onClick = { products.retry() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            SearchState.EMPTY_RESULTS -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found for: ${uiState.searchQuery}")
                }
            }

            SearchState.ERROR -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.error ?: "Unknow error")
                    Button(
                        onClick = onRetry
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}