package model

@kotlinx.serialization.Serializable
data class Author(
    val id: Int,
    val name: String,
    val content: String,
)