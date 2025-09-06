package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.repository.RecentSearchRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRecentSearchesUseCaseTest {

    private lateinit var repository: RecentSearchRepository
    private lateinit var getRecentSearchesUseCase: GetRecentSearchesUseCase

    @Before
    fun setUp() {
        clearAllMocks()
        repository = mockk(relaxed = true)
        getRecentSearchesUseCase = GetRecentSearchesUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenInvoke_whenCalled_thenCallRepositoryGetRecentSearches() = runTest {
        val userId = "user123"
        val recents = MockUtils.recentSearches

        coEvery { repository.getRecentSearches(userId) } returns
                DomainResult.Success(recents)
        val result = getRecentSearchesUseCase(userId)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data == recents)
        coVerify { repository.getRecentSearches(userId) }
    }

    @Test
    fun givenInvoke_whenCalledAndRepositoryReturnsError_thenReturnDomainResultError() = runTest {
        val userId = "user123"

        coEvery { repository.getRecentSearches(userId) } returns
                DomainResult.Error("DB Error", ErrorType.DATABASE)
        val result = getRecentSearchesUseCase(userId)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).type == ErrorType.DATABASE)
        coVerify { repository.getRecentSearches(userId) }
    }

}