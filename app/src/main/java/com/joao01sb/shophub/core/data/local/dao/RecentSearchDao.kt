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

    @Query("SELECT * FROM recent_searches ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(): List<RecentSearchEntity>


}