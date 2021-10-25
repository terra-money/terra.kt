package money.terra.client.rest

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

internal expect val ENGINE_FACTORY: EngineFactory<HttpClientEngineConfig>

internal expect fun String.asUrlEncoded(): String

const val DEFAULT_TIMEOUT_MILLIS: Long = 10000

class EngineFactory<T : HttpClientEngineConfig>(
    val engine: HttpClientEngineFactory<T>,
    val configure: T.() -> Unit = {},
)

class HttpClient(
    val serverUrl: String,
    val timeoutMillis: Long = 10000,
    val logConfig: (Logging.Config.() -> Unit)? = null,
    override val coroutineContext: CoroutineContext = Dispatchers.Default,
) : CoroutineScope {

    val baseUrl: String = if (serverUrl.endsWith("/")) serverUrl.dropLast(1) else serverUrl

    val server = HttpClient(ENGINE_FACTORY.engine) {
        engine(ENGINE_FACTORY.configure)

        install(JsonFeature) {
            serializer = AminoSerializer
        }

        install(HttpTimeout) {
            requestTimeoutMillis = timeoutMillis
        }

        if (logConfig != null) {
            install(Logging, logConfig)
        }
    }

    inline fun <reified T> get(
        path: String,
        queryParam: Map<String, String?> = emptyMap(),
    ): Deferred<T> {
        val query = if (queryParam.isEmpty()) {
            ""
        } else {
            queryParam.entries.joinToString("&", "?") { "${it.key}=${it.value}" }
        }

        return async {
            try {
                server.get(baseUrl + path + query)
            } catch (e: ResponseException) {
                throw RestClientResponseException(e.response.status.value, e.response.readText(), e)
            }
        }
    }

    inline fun <reified T> post(
        path: String,
        body: Any,
    ): Deferred<T> = async {
        try {
            server.post(baseUrl + path) {
                contentType(ContentType.Application.Json)

                this.body = body
            }
        } catch (e: ResponseException) {
            throw RestClientResponseException(e.response.status.value, e.response.readText(), e)
        }
    }
}

class RestClientResponseException(
    val httpCode: Int,
    val responseBody: String,
    cause: Throwable?,
) : Exception("Occur error in rest request", cause)
