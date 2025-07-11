package com.joao01sb.shophub.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey val query: String,
    val timestamp: Long = System.currentTimeMillis()
)