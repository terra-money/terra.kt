package money.terra.client.rest

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import java.net.URLEncoder

@Suppress("UNCHECKED_CAST")
internal actual val ENGINE_FACTORY: EngineFactory<HttpClientEngineConfig> = EngineFactory(CIO) {

} as EngineFactory<HttpClientEngineConfig>

internal actual fun String.asUrlEncoded(): String = URLEncoder.encode(this, Charsets.UTF_8.name())