package money.terra.proto.mapper.type

import com.google.protobuf.*
import kotlinx.datetime.*
import money.terra.type.Binary
import kotlin.time.Duration.Companion.seconds

fun Binary.toProtobuf(): ByteString = ByteString.copyFrom(data)

fun ByteString.fromProtobuf() = Binary(toByteArray())

fun DateTimePeriod.toProtobuf(): Duration = duration {
    val nano = this@toProtobuf.nanoseconds
    seconds = nano.toLong() / 1000
    nanos = nano % 1000
}

fun Duration.fromProtobuf() = DateTimePeriod(nanoseconds = seconds.seconds.inWholeNanoseconds + nanos.toLong())

fun LocalDateTime.toProtobuf(): Timestamp = timestamp {
    val time = this@toProtobuf.toInstant(TimeZone.UTC)
    seconds = time.epochSeconds
    nanos = time.nanosecondsOfSecond
}

fun Timestamp.fromProtobuf() = Instant.fromEpochSeconds(seconds, nanos).toLocalDateTime(TimeZone.UTC)
