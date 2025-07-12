package com.joao01sb.shophub.features.cart.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.cart.domain.datasource.CartRemoteDataSource
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CartRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : CartRemoteDataSource {

    override suspend fun getCartItems(userId: String): List<CartItem> {
        val snapshot = firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(CartItem::class.java) }
    }

    override suspend fun addOrUpdateItem(userId: String, item: CartItem) {
        firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(item.idProduto.toString())
            .set(item)
            .await()
    }

    override suspend fun removeItem(userId: String, productId: String) {
        firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(productId)
            .delete()
            .await()
    }

    override suspend fun clearCart(userId: String) {
        val cartRef = firestore
            .collection("users")
            .document(userId)
            .collection("cart")

        val snapshot = cartRef.get().await()
        snapshot.documents.forEach { it.reference.delete().await() }
    }

    override suspend fun placeOrder(
        userId: String,
        items: List<CartItem>,
        info: CheckoutInfo
    ) {
        val orderId = UUID.randomUUID().toString()

        val total = items.sumOf { it.precoUni * it.quantidade }

        val order = Order(
            id = orderId,
            items = items,
            total = total,
            status = "confirm",
            createdAt = System.currentTimeMillis(),
            paymentInfo = info
        )

        firestore
            .collection("users")
            .document(userId)
            .collection("orders")
            .document(orderId)
            .set(order)
            .await()
    }
}
