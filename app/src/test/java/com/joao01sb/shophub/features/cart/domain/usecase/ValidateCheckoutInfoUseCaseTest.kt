package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateCheckoutInfoUseCaseTest {

    private val validateCheckoutInfoUseCase = ValidateCheckoutInfoUseCase()

    @Test
    fun testValidCheckoutInfo() {
        val checkoutInfo = CheckoutInfo(
            numberCard = "1234567812345678",
            nameCard = "Joao",
            dateCard = "1225",
            cvvCard = "123",
            fullName = "Joao Silva",
            phoneNumber = "1234567890"
        )
        assertTrue(validateCheckoutInfoUseCase(checkoutInfo))
    }

    @Test
    fun testInvalidCheckoutInfo() {
        val checkoutInfo = CheckoutInfo(
            numberCard = "1234",
            nameCard = "",
            dateCard = "12",
            cvvCard = "1",
            fullName = "",
            phoneNumber = "123"
        )
        assertFalse(validateCheckoutInfoUseCase(checkoutInfo))
    }

}