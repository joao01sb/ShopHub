package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

class ValidateCheckoutInfoUseCase(
    val checkoutInfo: CheckoutInfo
) {

    operator fun invoke(): Boolean {
        return checkoutInfo.numberCard.isNotEmpty() &&
                checkoutInfo.nameCard.isNotEmpty() &&
                checkoutInfo.dateCard.isNotEmpty() &&
                checkoutInfo.cvvCard.isNotEmpty() &&
                checkoutInfo.fullName.isNotEmpty()
    }

}