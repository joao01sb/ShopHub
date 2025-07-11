package com.joao01sb.shophub.core.data.mapper

import com.joao01sb.shophub.core.data.local.entities.RecentSearchEntity
import com.joao01sb.shophub.core.domain.model.RecentSearch

fun RecentSearchEntity.toModel() = RecentSearch(
    query = query,
    timestamp = timestamp
)

fun RecentSearch.toEntity() = RecentSearchEntity(
    query = query,
    timestamp = timestamp
)