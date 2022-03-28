package model.common

interface ISortable<T: Enum<T>> {
    fun select(field: T): Any?
}

interface Sortable<T: Enum<T>> : ISortable<T> {
    override fun select(field: T): Comparable<*>?
}

fun <T: Enum<T>, R: Sortable<T>> Iterable<R>.sortedBy(
    field: T,
    order: Order = Order.Ascending,
): List<R> = sortedWith(
    when (order) {
        Order.Ascending -> compareBy { it.select(field) }
        Order.Descending -> compareByDescending { it.select(field) }
    }
)


enum class EmptyEnum
interface EmptySortable : Sortable<EmptyEnum> { override fun select(field: EmptyEnum) = null }