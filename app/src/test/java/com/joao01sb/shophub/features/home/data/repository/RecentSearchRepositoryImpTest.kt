package com.joao01sb.shophub.features.home.data.repository

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.datasource.RecentSearchDataSource
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.database.DatabaseResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.data.datasource.RecentSearchDataSourceImp
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
import java.sql.SQLException

class RecentSearchRepositoryImpTest {

    private lateinit var recentSearchDataSource: RecentSearchDataSource
    private lateinit var recentSearchRepository: RecentSearchRepository

    @Before
    fun setUp() {
        clearAllMocks()
        recentSearchDataSource = mockk<RecentSearchDataSourceImp>(relaxed = true)
        recentSearchRepository = RecentSearchRepositoryImp(recentSearchDataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenInsert_whenInsertRecentSearch_thenCallDataSourceInsert() = runTest {
        val newQuery = "laptop"
        val userId = "user123"

        coEvery { recentSearchDataSource.insert(any()) } returns DatabaseResult.Success(Unit)

        val result = recentSearchRepository.insert(
            RecentSearchEntity(
                queryKey = newQuery,
                userId = userId
            )
        )

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data == Unit)
        coVerify { recentSearchDataSource.insert(any()) }
    }

    @Test
    fun givenInsert_whenInsertRecentSearchAndDataSourceThrowsException_thenReturnDomainResultError() =
        runTest {
            val newQuery = "laptop"
            val userId = "user123"
            coEvery { recentSearchDataSource.insert(any()) } returns DatabaseResult.DatabaseError(
                SQLException("DB Error")
            )
            val result = recentSearchRepository.insert(
                RecentSearchEntity(
                    queryKey = newQuery,
                    userId = userId
                )
            )
            assert(result is DomainResult.Error)
            coVerify { recentSearchDataSource.insert(any()) }
        }

    @Test
    fun givenGetRecentSearches_whenCalled_thenCallDataSourceGetRecentSearches() = runTest {
        val userId = "user123"
        val recentSearches = MockUtils.recentSearches

        coEvery { recentSearchDataSource.getRecentSearches(userId) } returns DatabaseResult.Success(
            recentSearches
        )
        val result = recentSearchRepository.getRecentSearches(userId)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data == recentSearches)
        coVerify { recentSearchDataSource.getRecentSearches(userId) }
    }

    @Test
    fun givenGetRecentSearches_whenDataSourceEmpty_thenReturnEmptyList() = runTest {
        val userId = "user123"
        val recentSearches = listOf<RecentSearchEntity>()

        coEvery { recentSearchDataSource.getRecentSearches(userId) } returns DatabaseResult.Success(
            recentSearches
        )
        val result = recentSearchRepository.getRecentSearches(userId)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data.isEmpty())
        coVerify { recentSearchDataSource.getRecentSearches(userId) }
    }

    @Test
    fun givenGetRecentSearches_whenDataSourceThrowsException_thenReturnDomainResultError() =
        runTest {
            val userId = "user123"

            coEvery { recentSearchDataSource.getRecentSearches(userId) } returns DatabaseResult.DatabaseError(
                SQLException("DB Error")
            )
            val result = recentSearchRepository.getRecentSearches(userId)

            assert(result is DomainResult.Error)
            coVerify { recentSearchDataSource.getRecentSearches(userId) }
        }

    @Test
    fun givenClearRecentSearches_whenCalled_thenCallDataSourceClearRecentSearches() = runTest {
        val userId = "user123"
        val query = "laptop"

        coEvery {
            recentSearchDataSource.clearRecentSearches(
                userId,
                query
            )
        } returns DatabaseResult.Success(Unit)
        val result = recentSearchRepository.clearRecentSearches(userId, query)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data == Unit)
        coVerify { recentSearchDataSource.clearRecentSearches(userId, query) }
    }

    @Test
    fun givenClearRecentSearches_whenDataSourceThrowsException_thenReturnDomainResultError() =
        runTest {
            val userId = "user123"
            val query = "laptop"

            coEvery {
                recentSearchDataSource.clearRecentSearches(
                    userId,
                    query
                )
            } returns DatabaseResult.DatabaseError(SQLException("DB Error"))
            val result = recentSearchRepository.clearRecentSearches(userId, query)

            assert(result is DomainResult.Error)
            coVerify { recentSearchDataSource.clearRecentSearches(userId, query) }
        }

}