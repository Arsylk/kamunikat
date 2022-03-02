package model.api.auth.login

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)