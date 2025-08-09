package com.joao01sb.shophub.core.data.mapper

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.model.RecentSearch

fun RecentSearchEntity.toModel() = RecentSearch(
    userId = userId,
    query = queryKey,
    timestamp = timestamp
)

fun RecentSearch.toEntity() = RecentSearchEntity(
    userId = userId,
    queryKey = query,
    timestamp = timestamp
)