package model.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class Order {
    Ascending,
    Descending,
}

fun Order.invert(): Order = when (this) {
    Order.Ascending -> Order.Descending
    Order.Descending -> Order.Ascending
}

fun Order?.cycle(): Order? = when (this) {
    null -> Order.Ascending
    Order.Ascending -> Order.Descending
    Order.Descending -> null
}