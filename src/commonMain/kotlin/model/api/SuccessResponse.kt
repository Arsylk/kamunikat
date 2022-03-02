package model.api

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SuccessResponse(
    @SerialName("success")
    val success: Boolean
)