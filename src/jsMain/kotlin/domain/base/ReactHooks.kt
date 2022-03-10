package domain.base

import domain.koin.scopeId
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.context.GlobalContext
import org.koin.mp.KoinPlatformTools
import react.*


@Target(AnnotationTarget.FUNCTION)
annotation class FCScope

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class NonFCScope

@FCScope
fun useScope(tag: String? = null): CoroutineScope {
    val scope by useState {
        val scope = MainScope()
        console.log("coroutine scope created: ${scope.hashCode()} !")
        if (tag != null) scope + CoroutineName(tag) else scope
    }
    useEffectOnce {
        cleanup {
            scope.cancel()
            console.log("coroutine scope cancelled: ${scope.hashCode()} !")
        }
    }
    return scope
}


@FCScope
fun <T> produceState(
    initialValue: T,
    key: Any? = null,
    block: @NonFCScope suspend StateSetter<T>.() -> Unit
): T {
    val state = useState(initialValue)
    val scope by useState { MainScope() }

    useEffect(key ?: emptyArray<dynamic>()) {
        scope.launch {
            block.invoke(state.component2())
        }
        cleanup { scope.cancel() }
    }

    return state.component1()
}

@FCScope
fun <T> Flow<T>.collectAsState(
    initialValue: T
) = produceState(initialValue) {
    collect {
        this(it)
    }
}


@FCScope
fun <T> StateFlow<T>.collectAsState() = produceState(value) {
    collect {
        this(it)
    }
}

@FCScope
inline fun <reified T: ViewModel> useViewModel(): Lazy<T> {
    val triple by useState {
        val koin = GlobalContext.get()
        val scopeId = scopeId<T>()
        val (scope, clear) = KoinPlatformTools.synchronized(scopeId) {
            val currentScope = koin.getScopeOrNull(scopeId)
            if (currentScope == null) koin.createScope<T>(scopeId) to true
            else currentScope to false
        }
        Triple(scope, scope.inject<T>(), clear)
    }
    useEffectOnce {
        cleanup {
            val clear = triple.third
            if (clear) {
                val (scope, viewModel) = triple
                if (viewModel.isInitialized()) viewModel.value.clear()
                scope.close()
            }
        }
    }
    return triple.second
}

fun <Effect : UiEffect> EffectViewModel<Effect>.useUiEffect(
    scope: CoroutineScope? = null,
    block: suspend (Effect) -> Unit,
) {
    (scope ?: this.scope).launch(Dispatchers.Main) {
        effect.collect { effect ->
            block.invoke(effect)
        }
    }
}