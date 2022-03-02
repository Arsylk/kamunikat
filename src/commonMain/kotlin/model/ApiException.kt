package model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ApiException(

    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("stacktrace")
    val stacktrace: String
)