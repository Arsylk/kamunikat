package domain.base

import domain.auth.AuthService
import domain.auth.UserContext
import domain.koin.inject
import kotlinx.coroutines.*
import model.user.User
import react.*


val ReactCoroutineScope = createContext(MainScope())

@FCScope
fun ChildrenBuilder.provideCoroutineScope(
    tag: String? = null,
    block: @ReactDsl ChildrenBuilder.() -> Unit,
) {
    val parentScope = useContext(ReactCoroutineScope)
    val childScope by useState {
        val ctx = parentScope.coroutineContext
        val newCtx = ctx + Job(ctx[Job])
        CoroutineScope(if (tag != null) newCtx + CoroutineName(tag) else newCtx)
    }

    +ReactCoroutineScope.Provider.create {
        value = childScope
        block(this)
    }
}
