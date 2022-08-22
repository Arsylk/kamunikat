package model.api.publication

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import model.common.IntId
import model.common.Sortable

@kotlinx.serialization.Serializable
data class Publication(
    @SerialName("id")
    override val id: Int = IntId.Default,
    @SerialName("is_published")
    val isPublished: Boolean,
    @SerialName("is_periodical")
    val isPeriodical: Boolean,
    @SerialName("is_planned")
    val isPlanned: Boolean,
    @SerialName("is_licenced")
    val isLicenced: Boolean,
    @SerialName("position")
    val position: Int,
    @SerialName("title")
    val title: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("co_author")
    val coAuthor: String,
    @SerialName("translator")
    val translator: String,
    @SerialName("edition")
    val edition: String,
    @SerialName("volume")
    val volume: String,
    @SerialName("redactor")
    val redactor: String,
    @SerialName("college")
    val college: String,
    @SerialName("illustrator")
    val illustrator: String,
    @SerialName("place")
    val place: String,
    @SerialName("series")
    val series: String,
    @SerialName("year")
    val year: LocalDate?,
    @SerialName("publishing_house")
    val publishingHouse: String,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("dimensions")
    val dimensions: String,
    @SerialName("signature")
    val signature: String,
    @SerialName("isbn")
    val isbn: String,
    @SerialName("issn")
    val issn: String,
    @SerialName("ukd")
    val ukd: String,
    @SerialName("copyright")
    val copyright: String,
    @SerialName("shop_url")
    val shopUrl: String,
    @SerialName("origin")
    val origin: String,
    @SerialName("extra")
    val extra: String,
    @SerialName("description")
    val description: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
) : Sortable<PublicationField>, IntId {

    override fun select(field: PublicationField): Comparable<*> = when (field) {
        PublicationField.Id -> id
        PublicationField.Title -> title
    }
}

@kotlinx.serialization.Serializable
enum class PublicationField {
    Id, Title
}