package model.common

interface IntId {
    val id: Int
}

inline val Collection<IntId>.ids get() = map { it.id }.toSet()

fun <T: IntId, R: IntId> intIdComparator(t: T, r: R) = t.id == r.id