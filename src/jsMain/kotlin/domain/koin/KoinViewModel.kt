package domain.koin

import domain.base.ViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import domain.base.useViewModel
import org.koin.core.component.getScopeId


inline fun <reified T : ViewModel> Module.viewModel(noinline definition: Definition<T>) {
    scope<T> {
        scoped(definition = definition)
    }
}

/**
 * Create scope at the root of your React tree with [useViewModel]
 *
 * Will be cleared & disposed once component unmounts
 */
inline fun <reified T: ViewModel> viewModel() = GlobalContext.get().getScope(scopeId<T>()).inject<T>()

inline fun <reified T: ViewModel> getViewModel() = GlobalContext.get().getScope(scopeId<T>()).get<T>()

inline fun <reified T: ViewModel> scopeId(): String = "${T::class.getScopeId()}:${ViewModelScopeId}"
const val ViewModelScopeId = "<ViewModelScope>"