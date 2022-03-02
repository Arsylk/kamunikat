package domain.auth

import domain.base.FCScope
import domain.base.produceState
import domain.base.useScope
import domain.koin.inject
import kotlinx.coroutines.flow.collectLatest
import model.user.User
import mui.material.Button
import react.*
import react.router.useNavigate

val UserContext = createContext<User?>(null)


@FCScope
fun ChildrenBuilder.provideUser(block: @ReactDsl ChildrenBuilder.() -> Unit,) {
    val authService by inject<AuthService>()
    val user = produceState<User?>(null) {
        console.log("produce state init")
        authService.userFlow().collectLatest {
            console.log("produce state collected user: $it")
            this@produceState.invoke(it)
        }
    }

    +UserContext.Provider.create {
        value = user
        block(this)
    }
}

@FCScope
fun useRequiredUser(): User? {
    val nav = useNavigate()
    val user = useContext(UserContext)
    useEffect(user) { if (user == null) nav("/") }
    return user
}