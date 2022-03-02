package domain.base

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class EffectViewModel<Effect : UiEffect> : ViewModel() {
    private val _effect = Channel<Effect>()
    private val _error = Channel<Throwable>()
    private val loadingKeys = MutableStateFlow(emptySet<String>())

    val effect = _effect.receiveAsFlow()
        .shareIn(scope, SharingStarted.WhileSubscribed())
    val error = _error.receiveAsFlow()
        .shareIn(scope, SharingStarted.WhileSubscribed())

    protected suspend fun setEffect(effect: Effect) {
        _effect.send(effect)
    }

    fun enqueueEffect(effect: Effect) {
        scope.launch { _effect.send(effect) }
    }

    protected suspend fun setError(throwable: Throwable) {
        _error.send(throwable)
    }

    fun enqueueError(throwable: Throwable) {
        scope.launch { _error.send(throwable) }
    }

    protected fun errorAware(
        key: String = DefaultKey,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        single: Boolean = true,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        if (single && loadingKeys.value.contains(key)) return
        loadingKeys.update { it + key }
        scope.launch(dispatcher) {
            kotlin.runCatching {
                coroutineScope { block.invoke(this) }
            }.onFailure { t ->
                if (t !is CancellationException) setError(t)
            }
        }.invokeOnCompletion {
            loadingKeys.update { it - key }
        }
    }

    companion object {
        private const val DefaultKey = "<DefaultKey>"
    }
}

interface UiEffect