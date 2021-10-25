package money.terra.sdk.tools.transaction

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

interface SemaphoreProvider {

    fun acquire(vararg key: String): Deferred<Unit>

    fun acquire(keys: List<String>): Deferred<Unit>

    fun release(vararg key: String): Deferred<Unit>

    fun release(keys: List<String>): Deferred<Unit>
}

fun <T> SemaphoreProvider.withPermit(
    vararg address: String,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: suspend () -> T,
): Deferred<T> = CoroutineScope(coroutineContext).async {
    acquire(*address).await()

    try {
        block()
    } finally {
        release(*address).await()
    }
}

fun <T> SemaphoreProvider.withPermit(
    addresses: List<String>,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: suspend () -> T,
): Deferred<T> = CoroutineScope(coroutineContext).async {
    acquire(addresses).await()

    try {
        block()
    } finally {
        release(addresses).await()
    }
}

object LocalSemaphoreProvider : SemaphoreProvider {

    private val acquireSemaphore = Semaphore(1)
    private val semaphores = mutableMapOf<String, Semaphore>()

    var retryDelayMillis: Long = 50

    override fun acquire(vararg key: String): Deferred<Unit> = acquire(key.toList())

    override fun acquire(keys: List<String>): Deferred<Unit> = CoroutineScope(Dispatchers.Default).async {
        while (isActive) {
            try {
                acquireSemaphore.withPermit {
                    val semaphores = keys.map { semaphores.getOrPut(it) { Semaphore(1) } }

                    if (semaphores.any { it.availablePermits == 0 }) {
                        delay(retryDelayMillis)
                        return@withPermit
                    }

                    semaphores.forEach { it.acquire() }
                }
            } catch (e: CancellationException) {
                release(keys).await()
            } catch (e: kotlinx.coroutines.CancellationException) {
                release(keys).await()
            }
        }
    }

    override fun release(vararg key: String): Deferred<Unit> = release(key.toList())

    override fun release(keys: List<String>): Deferred<Unit> = CoroutineScope(Dispatchers.Default).async {
        withContext(NonCancellable) {
            acquireSemaphore.withPermit {
                keys.forEach { semaphores.remove(it)?.release() }
            }
        }
    }
}