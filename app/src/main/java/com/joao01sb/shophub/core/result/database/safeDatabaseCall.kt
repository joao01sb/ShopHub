package com.joao01sb.shophub.core.result.database

import com.joao01sb.shophub.core.result.database.DatabaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException

suspend fun <T> safeDatabaseCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    databaseCall: suspend () -> T
): DatabaseResult<T> {
    return withContext(dispatcher) {
        try {
            DatabaseResult.Success(databaseCall())
        } catch (e: SQLException) {
            DatabaseResult.DatabaseError(e)
        } catch (e: Exception) {
            DatabaseResult.UnknownError(e)
        }
    }
}

suspend fun <T> (() -> T).safeDatabaseCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): DatabaseResult<T> = safeDatabaseCall(dispatcher, this)