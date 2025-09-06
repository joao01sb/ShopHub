package com.joao01sb.shophub.features.home.utils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

suspend fun <T : Any> PagingData<T>.collectDataForTest(
    dispatcher: CoroutineDispatcher = Dispatchers.Main
): List<T> {
    val dcb = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }

    val differ = AsyncPagingDataDiffer(
        diffCallback = dcb,
        updateCallback = mockk(relaxed = true),
        workerDispatcher = dispatcher
    )

    differ.submitData(this)
    return differ.snapshot().items
}