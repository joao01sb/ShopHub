package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ClearSearchUseCaseTest {

    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var clearSearchUseCase: ClearSearchUseCase

    @Before
    fun setUp() {
        clearAllMocks()
        recentSearchRepository = mockk(relaxed = true)
        clearSearchUseCase = ClearSearchUseCase(recentSearchRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenInvoke_whenCalled_thenCallRepositoryClearRecentSearches() = runTest {
        val userId = "user123"
        val query = "laptop"

        coEvery { recentSearchRepository.clearRecentSearches(any(), any()) } returns
                DomainResult.Success(Unit)
        val result = clearSearchUseCase(userId, query)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data == Unit)
        coEvery { recentSearchRepository.clearRecentSearches(userId, query) }

    }

    @Test
    fun givenInvoke_whenCalledAndRepositoryThrowsException_thenReturnDomainResultError() =
        runTest {
            val userId = "user123"
            val query = "laptop"

            coEvery {
                recentSearchRepository.clearRecentSearches(
                    any(),
                    any()
                )
            } returns DomainResult.Error("DB Error", ErrorType.DATABASE)
            val result = clearSearchUseCase(userId, query)

            assert(result is DomainResult.Error)
            assert((result as DomainResult.Error).type == ErrorType.DATABASE)
            coEvery { recentSearchRepository.clearRecentSearches(userId, query) }
        }

}