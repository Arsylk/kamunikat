package model.api.auth

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RegisterRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("username")
    val username: String,
)