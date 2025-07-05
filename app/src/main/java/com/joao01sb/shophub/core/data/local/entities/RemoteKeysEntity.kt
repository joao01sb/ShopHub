package com.joao01sb.shophub.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val productId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)