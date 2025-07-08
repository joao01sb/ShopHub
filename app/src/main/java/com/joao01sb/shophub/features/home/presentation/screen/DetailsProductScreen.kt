package com.joao01sb.shophub.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.joao01sb.shophub.R
import com.joao01sb.shophub.features.home.presentation.state.ProductDetailsUiState
import com.joao01sb.shophub.shared_ui.components.TopAppBarCustom

@Preview
@Composable
fun DetailsProductScreen(
    uiState: ProductDetailsUiState = ProductDetailsUiState(),
    onBack: () -> Unit = {},
    onAddCart: () -> Unit = {}
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("progress_indicator_details_product_screen")
            ) {
                CircularProgressIndicator()
            }
        } else if (!uiState.error.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("progress_indicator_details_product_screen")
            ) {
                Text(uiState.error)
                Button(
                    onClick = onBack
                ) {
                    Text("Back")
                }
            }
        } else {
            TopAppBarCustom("Details", onNavigationClick = onBack)
            Column(
            ) {

                Box(modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.product?.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        placeholder = painterResource(
                            R.drawable.ic_launcher_foreground
                        )
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = uiState.product?.name ?: "Unknow product name",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Category: " + uiState.product?.category ?: "Unknow category",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = uiState.product?.description ?: "Unknow description",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )


                    Text(
                        text = "Stock: " + uiState.product?.stock.toString() ?: "Unknow Stock",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "R$ ${String.format("%.2f", uiState.product?.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Red
                    )


                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = onAddCart
                ) {
                    Text("Add to Cart",fontSize = 16.sp,)
                }

            }
        }
    }

}