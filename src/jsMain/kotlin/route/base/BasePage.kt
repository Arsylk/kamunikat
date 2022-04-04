package route.base

import csstype.FlexGrow
import csstype.number
import csstype.rem
import domain.auth.AuthService
import domain.auth.UserContext
import domain.base.useScope
import domain.isLoggedIn
import domain.koin.get
import domain.koin.inject
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.*
import mui.system.ThemeProvider
import react.*
import react.dom.html.ReactHTML
import react.router.Outlet
import react.router.useNavigate
import route.Route
import route.base.home.HomePage
import mui.icons.material.Menu as MenuIcon


val BasePage = FC<Props> {
    val navigate = useNavigate()
    val scope = useScope()
    val user = useContext(UserContext)

    ThemeProvider {
        theme = BaseTheme
        Box {
            sx = jso { flexGrow = number(1.0) }
            AppBar {
                position = AppBarPosition.static
                Toolbar {
                    IconButton {
                        size = Size.large
                        edge = IconButtonEdge.start
                        sx = jso { marginRight = 1.rem }

                        MenuIcon()
                    }
                    Typography {
                        sx = jso { flexGrow = number(1.0) }
                        variant = "h6"
                        component = ReactHTML.div

                        +"Toolbar"
                        if (user != null) +": ${user.username}"
                    }

                    Button {
                        color = ButtonColor.inherit
                        onClick = {
                            it.preventDefault()
                            if (!user.isLoggedIn) navigate("/login")
                            else scope.launch {
                                get<AuthService>().logout()
                                navigate("/")
                            }
                        }
                        +if (!user.isLoggedIn) "Login" else "Logout"
                    }
                }
            }
            Outlet()
        }
    }
}