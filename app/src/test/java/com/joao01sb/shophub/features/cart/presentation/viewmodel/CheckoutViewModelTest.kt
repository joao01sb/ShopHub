package com.joao01sb.shophub.features.cart.presentation.viewmodel

import app.cash.turbine.test
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.data.datasource.MockUtils.mockCartItems
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.usecase.ValidateCheckoutInfoUseCase
import com.joao01sb.shophub.features.cart.presentation.event.CheckoutEvent
import com.joao01sb.shophub.features.cart.presentation.state.CheckoutUiEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    private lateinit var viewModel: CheckoutViewModel
    private val validateCheckoutInfoUseCase: ValidateCheckoutInfoUseCase = mockk()
    private val cartManager: CartManager = mockk()
    private val authManager: AuthManager = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(testDispatcher)
        every { authManager.getCurrentUserId() } returns Result.success("userId")
        every { cartManager.getItems(any()) } returns flowOf(mockCartItems)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun givenCardNumberChanged_whenOnEvent_thenStateIsUpdated() = runTest {
        val cardNumber = "1234567890123456"

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.cardNumber.isEmpty())

            viewModel.onEvent(CheckoutEvent.CardNumberChanged(cardNumber))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState.cardNumber == cardNumber)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenCardNameChanged_whenOnEvent_thenStateIsUpdated() = runTest {
        val cardName = "João Silva"

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.cardHolderName.isEmpty())

            viewModel.onEvent(CheckoutEvent.CardNameChanged(cardName))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState.cardHolderName == cardName)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenCardValidationChanged_whenOnEvent_thenStateIsUpdated() = runTest {
        val expiryDate = "12/25"

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.expiryDate.isEmpty())

            viewModel.onEvent(CheckoutEvent.CardValidationChanged(expiryDate))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState.expiryDate == expiryDate)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenCardCVVChanged_whenOnEvent_thenStateIsUpdated() = runTest {
        val cvv = "123"

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.cvv.isEmpty())

            viewModel.onEvent(CheckoutEvent.CardCVVChanged(cvv))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState.cvv == cvv)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenFullNameChanged_whenOnEvent_thenStateIsUpdated() = runTest {
        val fullName = "João Silva Santos"

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.fullName.isEmpty())

            viewModel.onEvent(CheckoutEvent.FullNameChanged(fullName))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState.fullName == fullName)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenPhoneChanged_whenOnEvent_thenStateIsUpdated() = runTest {
        val phone = "+5511999999999"

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.phone.isEmpty())

            viewModel.onEvent(CheckoutEvent.PhoneChanged(phone))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState.phone == phone)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenValidCheckoutInfo_whenOnEvent_thenCheckoutIsSuccessful() = runTest {
        val checkoutInfo = CheckoutInfo(
            numberCard = "1234567890123456",
            nameCard = "João Silva",
            dateCard = "12/25",
            cvvCard = "123",
            fullName = "João Silva Santos",
            phoneNumber = "+5511999999999"
        )

        every { validateCheckoutInfoUseCase(any()) } returns true
        coEvery { cartManager.placeOrder(any(), any(), any()) } returns DomainResult.Success(Unit)

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.onEvent(CheckoutEvent.CardNumberChanged(checkoutInfo.numberCard))
        viewModel.onEvent(CheckoutEvent.CardNameChanged(checkoutInfo.nameCard))
        viewModel.onEvent(CheckoutEvent.CardValidationChanged(checkoutInfo.dateCard))
        viewModel.onEvent(CheckoutEvent.CardCVVChanged(checkoutInfo.cvvCard))
        viewModel.onEvent(CheckoutEvent.FullNameChanged(checkoutInfo.fullName))
        viewModel.onEvent(CheckoutEvent.PhoneChanged(checkoutInfo.phoneNumber))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.checkoutUiEvent.test {
            viewModel.onEvent(CheckoutEvent.Checkout)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CheckoutUiEvent.Finaly)

            cancelAndIgnoreRemainingEvents()
        }

        verify { validateCheckoutInfoUseCase(checkoutInfo) }
        coVerify { cartManager.placeOrder("userId", mockCartItems, checkoutInfo) }
    }

    @Test
    fun givenValidCheckoutInfo_whenOnEvent_thenCheckoutFails_error() = runTest {
        val checkoutInfo = CheckoutInfo(
            numberCard = "1234567890123456",
            nameCard = "João Silva",
            dateCard = "12/25",
            cvvCard = "123",
            fullName = "João Silva Santos",
            phoneNumber = "+5511999999999"
        )

        every { validateCheckoutInfoUseCase(any()) } returns true
        coEvery { cartManager.placeOrder(any(), any(), any()) } returns
                DomainResult.Error("Error placing order", ErrorType.UNKNOWN)

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.onEvent(CheckoutEvent.CardNumberChanged(checkoutInfo.numberCard))
        viewModel.onEvent(CheckoutEvent.CardNameChanged(checkoutInfo.nameCard))
        viewModel.onEvent(CheckoutEvent.CardValidationChanged(checkoutInfo.dateCard))
        viewModel.onEvent(CheckoutEvent.CardCVVChanged(checkoutInfo.cvvCard))
        viewModel.onEvent(CheckoutEvent.FullNameChanged(checkoutInfo.fullName))
        viewModel.onEvent(CheckoutEvent.PhoneChanged(checkoutInfo.phoneNumber))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.checkoutUiEvent.test {
            viewModel.onEvent(CheckoutEvent.Checkout)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CheckoutUiEvent.Error)
            assert((event as CheckoutUiEvent.Error).message == "Error placing order")

            cancelAndIgnoreRemainingEvents()
        }

        verify { validateCheckoutInfoUseCase(checkoutInfo) }
        coVerify { cartManager.placeOrder("userId", mockCartItems, checkoutInfo) }
    }

    @Test
    fun givenInvalidCheckoutInfo_whenOnEvent_thenValidationFails() = runTest {
        val checkoutInfo = CheckoutInfo(
            numberCard = "invalid",
            nameCard = "",
            dateCard = "",
            cvvCard = "",
            fullName = "",
            phoneNumber = ""
        )

        every { validateCheckoutInfoUseCase(any()) } returns false

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.onEvent(CheckoutEvent.CardNumberChanged(checkoutInfo.numberCard))
        viewModel.onEvent(CheckoutEvent.CardNameChanged(checkoutInfo.nameCard))
        viewModel.onEvent(CheckoutEvent.CardValidationChanged(checkoutInfo.dateCard))
        viewModel.onEvent(CheckoutEvent.CardCVVChanged(checkoutInfo.cvvCard))
        viewModel.onEvent(CheckoutEvent.FullNameChanged(checkoutInfo.fullName))
        viewModel.onEvent(CheckoutEvent.PhoneChanged(checkoutInfo.phoneNumber))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.checkoutUiEvent.test {
            viewModel.onEvent(CheckoutEvent.Checkout)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CheckoutUiEvent.Error)
            assert((event as CheckoutUiEvent.Error).message == "invalid checkout information")

            cancelAndIgnoreRemainingEvents()
        }

        verify { validateCheckoutInfoUseCase(checkoutInfo) }
        coVerify(exactly = 0) { cartManager.placeOrder(any(), any(), any()) }
    }

    @Test
    fun givenCheckoutWithException_whenOnEvent_thenErrorIsEmitted() = runTest {
        val checkoutInfo = CheckoutInfo(
            numberCard = "1234567890123456",
            nameCard = "João Silva",
            dateCard = "12/25",
            cvvCard = "123",
            fullName = "João Silva Santos",
            phoneNumber = "+5511999999999"
        )

        every { validateCheckoutInfoUseCase(any()) } returns true
        coEvery { cartManager.placeOrder(any(), any(), any()) } throws RuntimeException("Network error")

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.onEvent(CheckoutEvent.CardNumberChanged(checkoutInfo.numberCard))
        viewModel.onEvent(CheckoutEvent.CardNameChanged(checkoutInfo.nameCard))
        viewModel.onEvent(CheckoutEvent.CardValidationChanged(checkoutInfo.dateCard))
        viewModel.onEvent(CheckoutEvent.CardCVVChanged(checkoutInfo.cvvCard))
        viewModel.onEvent(CheckoutEvent.FullNameChanged(checkoutInfo.fullName))
        viewModel.onEvent(CheckoutEvent.PhoneChanged(checkoutInfo.phoneNumber))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.checkoutUiEvent.test {
            viewModel.onEvent(CheckoutEvent.Checkout)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CheckoutUiEvent.Error)
            assert((event as CheckoutUiEvent.Error).message == "Network error")

            cancelAndIgnoreRemainingEvents()
        }

        verify { validateCheckoutInfoUseCase(checkoutInfo) }
        coVerify { cartManager.placeOrder("userId", mockCartItems, checkoutInfo) }
    }

    @Test
    fun givenUserNotAuthenticated_whenViewModelInit_thenErrorEventIsEmitted() = runTest {
        every { authManager.getCurrentUserId() } returns Result.failure(Exception("User not authenticated"))

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutUiEvent.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CheckoutUiEvent.Error)
            assert((event as CheckoutUiEvent.Error).message == "User not authenticated")

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 0) { cartManager.getItems(any()) }
    }

    @Test
    fun givenCartItemsLoaded_whenViewModelInit_thenStateIsUpdatedWithItems() = runTest {
        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val updatedState = awaitItem()
            assert(updatedState.itens == mockCartItems)
            assert(updatedState.itens.size == mockCartItems.size)

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
    }

    @Test
    fun givenLoadingState_whenCheckoutStarts_thenIsLoadingIsTrue() = runTest {
        val checkoutInfo = CheckoutInfo(
            numberCard = "1234567890123456",
            nameCard = "João Silva",
            dateCard = "12/25",
            cvvCard = "123",
            fullName = "João Silva Santos",
            phoneNumber = "+5511999999999"
        )

        every { validateCheckoutInfoUseCase(any()) } returns true
        coEvery { cartManager.placeOrder(any(), any(), any()) } returns DomainResult.Success(Unit)

        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.onEvent(CheckoutEvent.CardNumberChanged(checkoutInfo.numberCard))
        viewModel.onEvent(CheckoutEvent.CardNameChanged(checkoutInfo.nameCard))
        viewModel.onEvent(CheckoutEvent.CardValidationChanged(checkoutInfo.dateCard))
        viewModel.onEvent(CheckoutEvent.CardCVVChanged(checkoutInfo.cvvCard))
        viewModel.onEvent(CheckoutEvent.FullNameChanged(checkoutInfo.fullName))
        viewModel.onEvent(CheckoutEvent.PhoneChanged(checkoutInfo.phoneNumber))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.checkoutSate.test {
            skipItems(1)

            viewModel.onEvent(CheckoutEvent.Checkout)

            val loadingState = awaitItem()
            assert(loadingState.isLoading)

            val finalState = awaitItem()
            assert(!finalState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenCompleteCheckoutFlow_whenAllFieldsAreUpdated_thenStateReflectsAllChanges() = runTest {
        viewModel = CheckoutViewModel(validateCheckoutInfoUseCase, cartManager, authManager)

        viewModel.checkoutSate.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState.cardNumber.isEmpty())
            assert(initialState.cardHolderName.isEmpty())
            assert(initialState.expiryDate.isEmpty())
            assert(initialState.cvv.isEmpty())
            assert(initialState.fullName.isEmpty())
            assert(initialState.phone.isEmpty())

            viewModel.onEvent(CheckoutEvent.CardNumberChanged("1234567890123456"))
            val state1 = awaitItem()
            assert(state1.cardNumber == "1234567890123456")

            viewModel.onEvent(CheckoutEvent.CardNameChanged("João Silva"))
            val state2 = awaitItem()
            assert(state2.cardHolderName == "João Silva")

            viewModel.onEvent(CheckoutEvent.CardValidationChanged("12/25"))
            val state3 = awaitItem()
            assert(state3.expiryDate == "12/25")

            viewModel.onEvent(CheckoutEvent.CardCVVChanged("123"))
            val state4 = awaitItem()
            assert(state4.cvv == "123")

            viewModel.onEvent(CheckoutEvent.FullNameChanged("João Silva Santos"))
            val state5 = awaitItem()
            assert(state5.fullName == "João Silva Santos")

            viewModel.onEvent(CheckoutEvent.PhoneChanged("+5511999999999"))
            val finalState = awaitItem()
            assert(finalState.phone == "+5511999999999")

            assert(finalState.cardNumber == "1234567890123456")
            assert(finalState.cardHolderName == "João Silva")
            assert(finalState.expiryDate == "12/25")
            assert(finalState.cvv == "123")
            assert(finalState.fullName == "João Silva Santos")
            assert(finalState.phone == "+5511999999999")

            cancelAndIgnoreRemainingEvents()
        }
    }
}