package model

@kotlinx.serialization.Serializable
data class SimpleData(
    val id: Int,
    val text: String,
)