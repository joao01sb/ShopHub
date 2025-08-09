package com.joao01sb.shophub.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity

@Dao
interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearch: RecentSearchEntity)

    @Query("SELECT * FROM recent_searches WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(userId: String): List<RecentSearchEntity>

    @Query("DELETE FROM recent_searches WHERE userId = :userId AND queryKey = :query")
    suspend fun clearRecentSearches(userId: String, query: String)

}