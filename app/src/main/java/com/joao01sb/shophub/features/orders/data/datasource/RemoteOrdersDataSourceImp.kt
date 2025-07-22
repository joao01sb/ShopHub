package com.joao01sb.shophub.features.orders.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.orders.domain.datasource.RemoteOrdersDataSource
import kotlinx.coroutines.tasks.await

class RemoteOrdersDataSourceImp(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : RemoteOrdersDataSource {

    override suspend fun getOrders(
        userId: String
    ): List<Order> {
        val snapshot = firestore
            .collection("users")
            .document(userId)
            .collection("orders")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun getOrderById(
        userId: String,
        orderId: String
    ): Order? {
        val document = firestore
            .collection("users")
            .document(userId)
            .collection("orders")
            .document(orderId)
            .get()
            .await()

        if (!document.exists()) {
            return null
        } else if (document.data.isNullOrEmpty()) {
            throw IllegalStateException("Order data is empty")
        }

        return document.toObject(Order::class.java)
    }
}