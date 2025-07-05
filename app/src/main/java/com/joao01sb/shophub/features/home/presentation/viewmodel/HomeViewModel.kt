package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.domain.usecase.GetAllproductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllproductsUseCase
) : ViewModel() {

    val products: Flow<PagingData<Product>> = getAllProductsUseCase()
        .cachedIn(viewModelScope)
}