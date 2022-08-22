package model.api.publication

import kotlinx.serialization.SerialName
import model.common.IntId

@kotlinx.serialization.Serializable
data class PublicationInventoryNumber(
    @SerialName("id")
    override val id: Int = IntId.Default,
    @SerialName("publication_id")
    val publicationId: Int,
    @SerialName("catalog_id")
    val catalogId: Int,
    @SerialName("text")
    val text: String,
): IntId