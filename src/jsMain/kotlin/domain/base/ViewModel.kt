package domain.base

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent

abstract class ViewModel : KoinComponent {
    val scope = MainScope()

    protected open fun onCleared() {}

    fun clear() {
        onCleared()
        scope.cancel()
    }
}