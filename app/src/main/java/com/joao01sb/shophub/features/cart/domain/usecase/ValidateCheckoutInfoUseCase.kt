package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

class ValidateCheckoutInfoUseCase {

    operator fun invoke(checkoutInfo: CheckoutInfo): Boolean {
        return checkoutInfo.numberCard.length == 16 &&
                checkoutInfo.nameCard.isNotEmpty() &&
                checkoutInfo.dateCard.length == 4 &&
                checkoutInfo.cvvCard.length >= 3 &&
                checkoutInfo.fullName.isNotEmpty() &&
                checkoutInfo.phoneNumber.length >= 10
    }


}