package model.api.author

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Author(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("aliases")
    val aliases: List<AuthorAlias>,
    @SerialName("has_info")
    val hasInfo: Boolean,
)