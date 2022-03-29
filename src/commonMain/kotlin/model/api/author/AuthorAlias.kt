package model.api.author

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AuthorAlias(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)