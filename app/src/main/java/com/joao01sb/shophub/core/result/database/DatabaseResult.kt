package com.joao01sb.shophub.core.result.database

import java.sql.SQLException

sealed class DatabaseResult<out T> {
    data class Success<T>(val data: T) : DatabaseResult<T>()
    data class DatabaseError(val exception: SQLException) : DatabaseResult<Nothing>()
    data class UnknownError(val exception: Exception) : DatabaseResult<Nothing>()
}