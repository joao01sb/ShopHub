package com.joao01sb.shophub.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.core.utils.ConstantsFloat
import com.joao01sb.shophub.sharedui.components.ProductCard
import com.joao01sb.shophub.sharedui.components.SearchHeader

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit = {},
    onClickProduct: (Int) -> Unit,
    products: LazyPagingItems<Product>
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchHeader(
            modifier = Modifier.fillMaxWidth(),
            onSearchClick = onSearchClick
        )
        LazyVerticalGrid(
            modifier = Modifier.weight(ConstantsFloat.const_10),
            columns = GridCells.Fixed(2),
        ) {
            items(products.itemCount) {
                products[it]?.let {
                    ProductCard(
                        product = it,
                        onClick = onClickProduct,
                    )
                }
            }
            item {
                if (products.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }

}