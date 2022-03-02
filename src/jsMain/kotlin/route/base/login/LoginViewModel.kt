package route.base.login

import domain.auth.AuthService
import domain.base.EffectViewModel
import domain.base.UiEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class LoginViewModel : EffectViewModel<Effect>() {
    private val authService by inject<AuthService>()

    init {
        if (authService.user != null) enqueueEffect(Effect.NavigateHome)
    }

    fun login(email: String, password: String) {
        scope.launch(Dispatchers.Default) {
            val result = authService.runCatching { login(email, password) }
            result.onSuccess { setEffect(Effect.NavigateHome) }
            result.onFailure { setEffect(Effect.LoginError(it)) }
        }
    }
}

sealed class Effect : UiEffect {
    object NavigateHome : Effect()
    data class LoginError(val throwable: Throwable) : Effect()
}