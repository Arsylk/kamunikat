package domain.base

import csstype.array
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import react.useEffect
import react.useEffectOnce


@FCScope
fun LaunchedEffect(effect: suspend () -> Unit, ) {
    val scope = useScope()
    useEffectOnce {
        val job = scope.launch { effect() }
        cleanup { job.cancel() }
    }
}

@FCScope
fun LaunchedEffect(
    key: Any?,
    effect: suspend () -> Unit,
) {
    val scope = useScope()
    useEffect(key) {
        val job = scope.launch { effect() }
        cleanup { job.cancel() }
    }
}

@FCScope
fun LaunchedEffect(
    key1: Any?,
    key2: Any?,
    effect: suspend () -> Unit,
) {
    val scope = useScope()
    useEffect(key1, key2) {
        val job = scope.launch { effect() }
        cleanup { job.cancel() }
    }
}