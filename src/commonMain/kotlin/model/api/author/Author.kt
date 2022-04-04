package model.api.author

import kotlinx.serialization.SerialName
import model.common.IntId

@kotlinx.serialization.Serializable
data class Author(
    @SerialName("id")
    override val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("aliases")
    val aliases: List<AuthorAlias>,
    @SerialName("has_info")
    val hasInfo: Boolean,
) : IntId