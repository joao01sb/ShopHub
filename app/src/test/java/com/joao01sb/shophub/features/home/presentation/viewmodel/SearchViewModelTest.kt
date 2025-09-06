package com.joao01sb.shophub.features.home.presentation.viewmodel

import app.cash.turbine.test
import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.model.SearchResult
import com.joao01sb.shophub.features.home.domain.state.SearchState
import com.joao01sb.shophub.features.home.domain.usecase.ClearSearchUseCase
import com.joao01sb.shophub.features.home.domain.usecase.GetRecentSearchesUseCase
import com.joao01sb.shophub.features.home.domain.usecase.SaveRecentSearchUseCase
import com.joao01sb.shophub.features.home.domain.usecase.SearchProductsUseCase
import com.joao01sb.shophub.features.home.presentation.event.SearchEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private lateinit var recentSearchesUseCase: GetRecentSearchesUseCase
    private lateinit var saveRecentSearchUseCase: SaveRecentSearchUseCase
    private lateinit var clearSearchUseCase: ClearSearchUseCase
    private lateinit var authManager: AuthManager
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()

        searchProductsUseCase = mockk(relaxed = true)
        recentSearchesUseCase = mockk(relaxed = true)
        saveRecentSearchUseCase = mockk(relaxed = true)
        clearSearchUseCase = mockk(relaxed = true)
        authManager = mockk(relaxed = true)

        setupDefaultMocks()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun setupDefaultMocks() {
        every { authManager.getCurrentUserId() } returns Result.success("test_user_id")
        coEvery { recentSearchesUseCase("test_user_id") } returns
                DomainResult.Success(MockUtils.recentSearches)
        coEvery { saveRecentSearchUseCase(any(), any()) } returns
                DomainResult.Success(Unit)
    }

    private fun createViewModel() {
        viewModel = SearchViewModel(
            searchProductsUseCase,
            recentSearchesUseCase,
            saveRecentSearchUseCase,
            clearSearchUseCase,
            authManager
        )
    }

    private fun createMockProducts(): List<Product> {
        return MockUtils.productsDto.map { it.toDomain() }
    }

    @Test
    fun givenRecentSearchClicked_whenOnEvent_thenSearchIsPerformedSuccessfully() = runTest {
        val searchQuery = "test search"
        val mockProducts = createMockProducts()
        val searchResponse = SearchResult(results = mockProducts, hasMore = false)

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Success(searchResponse)

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.RecentSearchClicked(searchQuery))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchQuery == searchQuery) {
                "Expected query: $searchQuery, got: ${state.searchQuery}"
            }
            assert(state.searchResults == mockProducts) { "Search results don't match" }
            assert(state.searchState == SearchState.RESULTS) {
                "Expected RESULTS state, got: ${state.searchState}"
            }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.error == null) {
                "Error should be null, got: ${state.error}"
            }
        }
    }

    @Test
    fun givenRecentSearchClicked_whenSearchFails_thenErrorStateIsSet() = runTest {
        val searchQuery = "test search"
        val errorMessage = "Search failed"

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Error(
                    errorMessage,
                    ErrorType.UNKNOWN
                )

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.RecentSearchClicked(searchQuery))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchQuery == searchQuery) {
                "Expected query: $searchQuery, got: ${state.searchQuery}"
            }
            assert(state.searchState == SearchState.ERROR) { "Expected ERROR state, got: ${state.searchState}" }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.error == errorMessage) {
                "Expected error: $errorMessage, got: ${state.error}"
            }
        }
    }

    @Test
    fun givenSearchEvent_whenQueryIsValid_thenSearchIsPerformedSuccessfully() = runTest {
        val searchQuery = "valid query"
        val mockProducts = createMockProducts()
        val searchResponse = SearchResult(results = mockProducts, hasMore = true)

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Success(searchResponse)

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.Search)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchResults == mockProducts) { "Search results don't match" }
            assert(state.searchState == SearchState.RESULTS) {
                "Expected RESULTS state, got: ${state.searchState}"
            }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.hasMoreResults) { "Should have more results" }
            assert(state.error == null) { "Error should be null, got: ${state.error}" }
        }
    }

    @Test
    fun givenSearchEvent_whenSearchFails_thenErrorStateIsSet() = runTest {
        val searchQuery = "valid query"
        val errorMessage = "Network error"

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Error(
                    errorMessage,
                    ErrorType.UNKNOWN
                )

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.Search)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchState == SearchState.ERROR) {
                "Expected ERROR state, got: ${state.searchState}"
            }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.error == errorMessage) {
                "Expected error: $errorMessage, got: ${state.error}"
            }
        }
    }

    @Test
    fun givenLoadMoreEvent_whenHasMoreResults_thenMoreResultsAreLoadedSuccessfully() = runTest {
        val searchQuery = "test query"
        val initialProducts = listOf(MockUtils.productsDto.first().toDomain())
        val moreProducts = listOf(MockUtils.productsDto[1].toDomain())

        val initialResponse = SearchResult(results = initialProducts, hasMore = true)
        val moreResponse = SearchResult(results = moreProducts, hasMore = false)

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Success(initialResponse)
        coEvery { searchProductsUseCase(searchQuery, 2) } returns
                DomainResult.Success(moreResponse)

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()
        viewModel.onEvent(SearchEvent.Search)
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.LoadMore)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchResults.size == 2) {
                "Expected 2 results, got: ${state.searchResults.size}"
            }
            assert(state.searchResults.containsAll(initialProducts + moreProducts)) {
                "Combined results don't match"
            }
            assert(!state.isLoadingMore) { "Should not be loading more" }
            assert(!state.hasMoreResults) { "Should not have more results" }
            assert(state.error == null) { "Error should be null, got: ${state.error}" }
        }
    }

    @Test
    fun givenLoadMoreEvent_whenLoadMoreFails_thenErrorStateIsSet() = runTest {
        val searchQuery = "test query"
        val initialProducts = listOf(MockUtils.productsDto.first().toDomain())
        val errorMessage = "Load more failed"

        val initialResponse = SearchResult(results = initialProducts, hasMore = true)

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Success(initialResponse)
        coEvery { searchProductsUseCase(searchQuery, 2) } returns
                DomainResult.Error(
                    errorMessage,
                    ErrorType.UNKNOWN
                )

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()
        viewModel.onEvent(SearchEvent.Search)
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.LoadMore)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchState == SearchState.ERROR) {
                "Expected ERROR state, got: ${state.searchState}"
            }
            assert(!state.isLoadingMore) { "Should not be loading more" }
            assert(state.error == errorMessage) {
                "Expected error: $errorMessage, got: ${state.error}"
            }
        }
    }

    @Test
    fun givenRetryEvent_whenRetryIsSuccessful_thenSearchResultsAreLoaded() = runTest {
        val searchQuery = "retry query"
        val mockProducts = createMockProducts()
        val searchResponse = SearchResult(results = mockProducts, hasMore = false)

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Success(searchResponse)

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.Retry)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchResults == mockProducts) { "Search results don't match" }
            assert(state.searchState == SearchState.RESULTS) {
                "Expected RESULTS state, got: ${state.searchState}"
            }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.error == null) {
                "Error should be null, got: ${state.error}"
            }
        }
    }

    @Test
    fun givenRetryEvent_whenRetryFails_thenErrorStateRemains() = runTest {
        val searchQuery = "retry query"
        val errorMessage = "Retry failed"

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Error(
                    errorMessage,
                    ErrorType.UNKNOWN
                )

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()
        viewModel.onEvent(SearchEvent.Retry)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchState == SearchState.ERROR) {
                "Expected ERROR state, got: ${state.searchState}"
            }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.error == errorMessage) {
                "Expected error: $errorMessage, got: ${state.error}"
            }
        }

    }

    @Test
    fun givenClearErrorEvent_whenOnEvent_thenErrorIsClearedSuccessfully() = runTest {
        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.ClearError)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.error == null) { "Error should be null after clearing" }
        }
    }

    @Test
    fun givenClearRecentSearchesEvent_whenClearIsSuccessful_thenRecentSearchesAreUpdated() =
        runTest {
            val queryToClear = "query to clear"
            val updatedRecentSearches = listOf(MockUtils.recentSearches.first())

            coEvery { clearSearchUseCase("test_user_id", queryToClear) } returns
                    DomainResult.Success(Unit)
            coEvery { recentSearchesUseCase("test_user_id") } returnsMany listOf(
                DomainResult.Success(MockUtils.recentSearches),
                DomainResult.Success(updatedRecentSearches)
            )

            createViewModel()
            advanceUntilIdle()

            viewModel.onEvent(SearchEvent.ClearRecentSearches(queryToClear))
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assert(state.recentSearches.size == 1) {
                    "Expected 1 recent search after clear, got: ${state.recentSearches.size}"
                }
            }

            coVerify { clearSearchUseCase("test_user_id", queryToClear) }
        }

    @Test
    fun givenClearRecentSearchesEvent_whenClearFails_thenRecentSearchesRemainUnchanged() = runTest {
        val queryToClear = "query to clear"
        val errorMessage = "Clear failed"

        coEvery { clearSearchUseCase("test_user_id", queryToClear) } returns
                DomainResult.Error(
                    errorMessage,
                    ErrorType.UNKNOWN
                )

        createViewModel()
        advanceUntilIdle()

        val originalSearchesCount = viewModel.uiState.value.recentSearches.size

        viewModel.onEvent(SearchEvent.ClearRecentSearches(queryToClear))
        advanceUntilIdle()

        assert(viewModel.uiState.value.recentSearches.size == originalSearchesCount) {
            "Recent searches count should remain unchanged on clear failure"
        }

        coVerify { clearSearchUseCase("test_user_id", queryToClear) }
    }

    @Test
    fun givenAuthFailure_whenViewModelIsCreated_thenErrorStateIsSet() = runTest {
        every { authManager.getCurrentUserId() } returns
                Result.failure(IOException("Auth failed"))

        createViewModel()
        advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assert(currentState.error?.contains("User not authenticated") == true) {
            "Expected auth error, got: ${currentState.error}"
        }
        assert(currentState.recentSearches.isEmpty()) {
            "Recent searches should be empty on auth failure"
        }
    }

    @Test
    fun givenEmptySearchResults_whenSearchIsPerformed_thenEmptyResultsStateIsSet() = runTest {
        val searchQuery = "no results query"
        val emptyResponse = SearchResult(results = emptyList(), hasMore = false)

        coEvery { searchProductsUseCase(searchQuery, 1) } returns
                DomainResult.Success(emptyResponse)

        createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.QueryChanged(searchQuery))
        advanceUntilIdle()

        viewModel.onEvent(SearchEvent.Search)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.searchResults.isEmpty()) { "Search results should be empty" }
            assert(state.searchState == SearchState.EMPTY_RESULTS) {
                "Expected EMPTY_RESULTS state, got: ${state.searchState}"
            }
            assert(!state.isSearching) { "Should not be searching" }
            assert(state.error == null) { "Error should be null, got: ${state.error}" }
        }
    }
}