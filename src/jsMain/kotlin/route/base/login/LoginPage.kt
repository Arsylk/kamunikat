package route.base.login

import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import csstype.rem
import domain.base.useUiEffect
import domain.base.useViewModel
import domain.koin.getViewModel
import domain.onValueChanged
import kotlinx.js.jso
import mui.material.*
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.router.RouteProps
import react.router.useNavigate
import route.Route
import route.base.home.HomePageRoute


object LoginPageRoute : Route {
    override val path = "login"
    override val name = "Login"
    override val parent = HomePageRoute

    override fun create(props: RouteProps) = LoginPage.create()
}

val LoginPage = FC<Props> {
    val viewModel by useViewModel<LoginViewModel>()
    val navigate = useNavigate()

    var formEmail by useState("")
    var formPassword by useState("")
    var formError by useState<String?>(null)

    viewModel.useUiEffect { effect ->
        when (effect) {
            is Effect.LoginError -> formError = effect.throwable.toString()
            Effect.NavigateHome -> navigate(HomePageRoute.absolutePath)
        }
    }

    Container {
        component = ReactHTML.main
        Box {
            sx = jso {
                marginTop = 8.rem
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = AlignItems.center
            }

            Typography {
                component = ReactHTML.h1
                variant = "h5"
                +"Log in"
            }
            formError?.let {
                Typography {
                    component = ReactHTML.h1
                    variant = "h5"
                    +it
                }
            }

            Box {
                component = ReactHTML.form
                onSubmit = { event ->
                    event.preventDefault()
                    getViewModel<LoginViewModel>().login(formEmail, formPassword)
                }

                TextField {
                    required = true
                    margin = FormControlMargin.normal
                    fullWidth = true
                    id = "email"
                    name = "email"
                    autoComplete = "email"
                    autoFocus = true
                    label = ReactNode("Email")
                    type = InputType.email

                    value = formEmail
                    onValueChanged { formEmail = it }
                }

                TextField {
                    required = true
                    margin = FormControlMargin.normal
                    fullWidth = true
                    id = "password"
                    name = "email"
                    autoComplete = "current-password"
                    label = ReactNode("Password")
                    type = InputType.password

                    value = formPassword
                    onValueChanged { formPassword = it }
                }

                Button {
                    type = ButtonType.submit
                    variant = ButtonVariant.contained
                    fullWidth = true
                    +"Log in"
                }
            }
        }
    }
}