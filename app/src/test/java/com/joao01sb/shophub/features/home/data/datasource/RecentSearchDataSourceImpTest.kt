package com.joao01sb.shophub.features.home.data.datasource

import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteBlobTooBigException
import com.joao01sb.shophub.core.data.local.dao.RecentSearchDao
import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.datasource.RecentSearchDataSource
import com.joao01sb.shophub.core.result.database.DatabaseResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecentSearchDataSourceImpTest {

    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var recentSearchDataSourceImp: RecentSearchDataSource

    @Before
    fun setUp() {
        clearAllMocks()
        recentSearchDao = mockk<RecentSearchDao>()
        recentSearchDataSourceImp = RecentSearchDataSourceImp(recentSearchDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenInsert_whenInsertRecentSearch_thenCallDaoInsert() = runTest {
        val newQuery = "laptop"
        val userId = "user123"

        coEvery { recentSearchDao.insert(any()) } returns Unit

        val result = recentSearchDataSourceImp.insert(
            RecentSearchEntity(
                queryKey = newQuery,
                userId = userId
            )
        )

        assert(result is DatabaseResult.Success)
        coVerify { recentSearchDao.insert(any()) }

    }

    @Test
    fun givenInsert_whenInsertRecentSearchAndDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val newQuery = "laptop"
        val userId = "user123"

        coEvery { recentSearchDao.insert(any()) } throws SQLiteBlobTooBigException("DB Error")

        val result = recentSearchDataSourceImp.insert(
            RecentSearchEntity(
                queryKey = newQuery,
                userId = userId
            )
        )

        assert(result is DatabaseResult.UnknownError)
        coVerify { recentSearchDao.insert(any()) }

    }

    @Test
    fun givenGetRecentSearches_whenCalled_thenCallDaoGetRecentSearches() = runTest {
        val userId = "user123"
        val recentSearches = listOf(
            RecentSearchEntity("laptop", userId),
            RecentSearchEntity("phone", userId)
        )

        coEvery { recentSearchDao.getRecentSearches(userId) } returns recentSearches
        val result = recentSearchDataSourceImp.getRecentSearches(userId)

        assert(result is DatabaseResult.Success)
        assert((result as DatabaseResult.Success).data == recentSearches)
        coVerify { recentSearchDao.getRecentSearches(userId) }

    }

    @Test
    fun givenGetRecentSearches_whenNoRecentSearches_thenReturnEmptyList() = runTest {
        val userId = "user123"
        val recentSearches = emptyList<RecentSearchEntity>()

        coEvery { recentSearchDao.getRecentSearches(userId) } returns recentSearches
        val result = recentSearchDataSourceImp.getRecentSearches(userId)

        assert(result is DatabaseResult.Success)
        assert((result as DatabaseResult.Success).data.isEmpty())
        coVerify { recentSearchDao.getRecentSearches(userId) }

    }

    @Test
    fun givenGetRecentSearches_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val userId = "user123"

        coEvery { recentSearchDao.getRecentSearches(userId) } throws SQLiteAbortException("DB Error")
        val result = recentSearchDataSourceImp.getRecentSearches(userId)

        assert(result is DatabaseResult.UnknownError)
        coVerify { recentSearchDao.getRecentSearches(userId) }

    }

    @Test
    fun givenClearRecentSearches_whenCalled_thenCallDaoClearRecentSearches() = runTest {
        val userId = "user123"
        val query = "laptop"

        coEvery { recentSearchDao.clearRecentSearches(userId, query) } returns Unit

        val result = recentSearchDataSourceImp.clearRecentSearches(userId, query)

        assert(result is DatabaseResult.Success)
        coVerify { recentSearchDao.clearRecentSearches(userId, query) }

    }

    @Test
    fun givenClearRecentSearches_whenDaoThrowsException_thenReturnDatabaseResultError() = runTest {
        val userId = "user123"
        val query = "laptop"

        coEvery {
            recentSearchDao.clearRecentSearches(
                userId,
                query
            )
        } throws SQLiteAbortException("DB Error")
        val result = recentSearchDataSourceImp.clearRecentSearches(userId, query)

        assert(result is DatabaseResult.UnknownError)
        coVerify { recentSearchDao.clearRecentSearches(userId, query) }

    }
}