package com.joao01sb.shophub.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.joao01sb.shophub.features.home.presentation.state.ProductDetailsUiState
import com.joao01sb.shophub.shared_ui.components.TopAppBarCustom

@Composable
fun DetailsProductScreen(
    uiState: ProductDetailsUiState,
    onBack:() -> Unit = {},
    onAddCart:() -> Unit = {}
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
            TopAppBarCustom()
            Column {

            }
        }
    }

}