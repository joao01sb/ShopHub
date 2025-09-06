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

class SaveRecentSearchUseCaseTest {

    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var saveRecentSearchUseCase: SaveRecentSearchUseCase

    @Before
    fun setUp() {
        clearAllMocks()
        recentSearchRepository = mockk(relaxed = true)
        saveRecentSearchUseCase = SaveRecentSearchUseCase(recentSearchRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenInvoke_whenCalled_thenCallRepositoryInsert() =
        runTest {
            val userId = "user123"
            val query = "laptop"

            coEvery { recentSearchRepository.insert(any()) } returns
                    DomainResult.Success(Unit)
            val result = saveRecentSearchUseCase(userId, query)

            assert(result is DomainResult.Success)
            assert((result as DomainResult.Success).data == Unit)
        }

    @Test
    fun givenInvoke_whenCalledAndRepositoryThrowsException_thenReturnDomainResultError() =
        runTest {
            val userId = "user123"
            val query = "laptop"

            coEvery { recentSearchRepository.insert(any()) } returns
                    DomainResult.Error(
                        "DB Error",
                        ErrorType.DATABASE
                    )
            val result = saveRecentSearchUseCase(userId, query)

            assert(result is DomainResult.Error)
            assert((result as DomainResult.Error).type == ErrorType.DATABASE)
        }

}