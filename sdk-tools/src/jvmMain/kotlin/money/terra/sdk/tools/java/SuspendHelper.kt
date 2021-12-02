@file:JvmName("SuspendHelper")

package money.terra.sdk.tools.java

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

fun <T> runBlocking(block: suspend () -> T): T = runBlocking { block() }

fun <T> toFuture(deferred: Deferred<T>): CompletableFuture<T> = deferred.asCompletableFuture()
