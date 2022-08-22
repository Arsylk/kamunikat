package model.api.periodical

import kotlinx.serialization.SerialName
import model.common.IntId

@kotlinx.serialization.Serializable
data class Periodical(
    @SerialName("id")
    override val id: Int = IntId.Default,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String?,
    @SerialName("is_published")
    val isPublished: Boolean,
) : IntId