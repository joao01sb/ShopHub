package com.joao01sb.shophub.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.state.SearchState
import com.joao01sb.shophub.features.home.presentation.state.SearchUiState
import com.joao01sb.shophub.shared_ui.components.ProductCard
import com.joao01sb.shophub.shared_ui.components.RecentSearchesContent
import com.joao01sb.shophub.shared_ui.components.SearchProductBar

@Composable
fun SearchProductScreen(
    uiState: SearchUiState,
    onNavigateToProduct: (Product) -> Unit,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onClearRecentSearches: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
                    onRecentSearchClick = onRecentSearchClick,
                    onClearRecentSearches = onClearRecentSearches
                )
            }

            SearchState.SEARCHING -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            SearchState.RESULTS -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(uiState.searchResults.size) { index ->
                        val product = uiState.searchResults[index]

                        ProductCard(
                            product = product,
                            onClick = { onNavigateToProduct(product) }
                        )

                        if (index >= uiState.searchResults.size - 2 &&
                            uiState.hasMoreResults &&
                            !uiState.isLoadingMore) {
                            LaunchedEffect(Unit) {
                                onLoadMore()
                            }
                        }
                    }

                    if (uiState.isLoadingMore) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }

            SearchState.EMPTY_RESULTS -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Empty results for query:")
                        Text(
                            text = "\"${uiState.searchQuery}\"",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            SearchState.ERROR -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = uiState.error ?: "Erro desconhecido")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
        }
    }
}