package model.common

interface ISortSelectable<T: Enum<T>> {
    fun select(field: T): Any
}

interface SortSelectable<T: Enum<T>> : ISortSelectable<T> {
    override fun select(field: T): Comparable<*>
}

fun <T: Enum<T>, R: SortSelectable<T>> Iterable<R>.sortedBy(
    field: T,
    order: Order = Order.Ascending,
): List<R> = sortedWith(
    when (order) {
        Order.Ascending -> compareBy { it.select(field) }
        Order.Descending -> compareByDescending { it.select(field) }
    }
)