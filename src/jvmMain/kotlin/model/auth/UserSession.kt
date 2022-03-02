package model.auth

import io.ktor.auth.*

@kotlinx.serialization.Serializable
data class UserSession(val userId: Int) : Principal