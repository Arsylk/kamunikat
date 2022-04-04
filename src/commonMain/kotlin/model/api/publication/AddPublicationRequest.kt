package model.api.publication

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddPublicationRequest(
    @SerialName("publication")
    val publication: Publication,
    @SerialName("periodical_ids")
    val periodicalIds: Set<Int> = emptySet(),
    @SerialName("catalog_ids")
    val catalogIds: Set<Int> = emptySet(),
    @SerialName("category_ids")
    val categoryIds: Set<Int> = emptySet(),
    @SerialName("author_ids")
    val authorIds: Set<Int> = emptySet(),
)