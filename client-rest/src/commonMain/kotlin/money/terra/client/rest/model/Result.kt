package money.terra.client.rest.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer

@Serializable
data class Result<T>(
    @Serializable(LongAsStringSerializer::class) val height: Long,
    val result: T,
)
