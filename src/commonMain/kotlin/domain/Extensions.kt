package domain

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
inline fun <T, R> use(value: T?, block: (T) -> R): R? {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
    return if (value != null) block.invoke(value) else null
}

@OptIn(ExperimentalContracts::class)
inline fun <T, R, N> use(value1: T?, value2: R?, block: (T, R) -> N): N? {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
    return if (value1 != null && value2 != null) block.invoke(value1, value2) else null
}