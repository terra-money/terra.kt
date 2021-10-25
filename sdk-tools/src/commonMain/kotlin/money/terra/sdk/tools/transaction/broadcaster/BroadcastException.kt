package money.terra.sdk.tools.transaction.broadcaster

import money.terra.sdk.tools.transaction.EstimateFeeException

sealed class BroadcastException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    object NotSigned : BroadcastException("Not signed transaction")
    object EmptyFee : BroadcastException("Not set fee")
    object UnknownAccountInfo : BroadcastException("Unknown account info")
    class FailedEstimateFee(exception: EstimateFeeException) : BroadcastException(exception.reason, exception)
}