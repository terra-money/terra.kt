package util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

actual val testCoroutineContext: CoroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) = runBlocking(testCoroutineContext) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}