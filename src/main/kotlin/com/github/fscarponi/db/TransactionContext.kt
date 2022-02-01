package com.github.fscarponi.db


import io.ktor.application.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.commitTransactionAndAwait


data class TransactionContext(override val di: DI) : DIAware {

    val client: CoroutineClient by instance()
    private val db
        get() = client.getDatabase(direct.instance("DATABASE_NAME"))

    //User
    val usersCollection: CoroutineCollection<User> by lazy { db.getCollection("users") }


}

suspend inline fun <R> TransactionContext.transaction(
    closeClient: Boolean = true,
    action: TransactionContext.() -> R
): R = try {
    client.startSession().use {
        it.startTransaction()
        val r = action()
        it.commitTransactionAndAwait()
        return r
    }
} finally {
    if (closeClient)
        withContext(Dispatchers.IO) {
            @Suppress("BlockingMethodInNonBlockingContext")
            client.close()
        }
}


suspend inline fun <T> PipelineContext<Unit, ApplicationCall>.databaseTransaction(
    closeClient: Boolean = true,
    action: TransactionContext.() -> T
) =
    closestDI().direct.instance<TransactionContext>().transaction(closeClient, action)
