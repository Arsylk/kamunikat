package model.common

interface IntId {
    val id: Int

    companion object {
        const val Default = 0
    }
}

inline val Collection<IntId>.ids get() = map { it.id }.toSet()

fun <T: IntId, R: IntId> intIdComparator(t: T, r: R) = t.id == r.id