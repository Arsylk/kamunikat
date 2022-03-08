package route.admin.users

import component.autocomplete.UserTagAutocomplete
import csstype.*
import domain.api.ApiService
import domain.base.produceState
import domain.koin.get
import domain.onValueChanged
import kotlinx.js.jso
import model.api.user.AddUserRequest
import model.user.UserTag
import mui.material.*
import mui.system.ResponsiveStyleValue
import mui.system.Theme
import mui.system.useTheme
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.li
import react.router.RouteProps
import route.Route
import route.admin.AdminPageRoute

object AdminUserAddPageRoute : Route {
    override val path = "user/add"
    override val name = "Add User"
    override val parent = AdminPageRoute

    override fun create(props: RouteProps) = AdminUserAddPage.create()
}

val AdminUserAddPage = FC<Props> {
    val (form, updateForm) = useState {
        AddUserRequest("", "", "", emptySet())
    }

    Box {
        sx = jso {
            flexGrow = FlexGrow(1.0)
            padding = Padding(2.em, 2.em)
        }
        Paper {
            sx = jso {
                padding = Padding(1.em, 1.em)
            }
            Typography {
                component = ReactHTML.h1
                variant = "h5"
                +"Add user"
            }

            Box {
                component = ReactHTML.form
                onSubmit = { event ->
                    event.preventDefault()
                    console.log(form)
                }
                id = "add-user"

                Grid {
                    container = true
                    spacing = ResponsiveStyleValue(2)
                    rowSpacing = ResponsiveStyleValue(2)

                    Grid {
                        item = true
                        xs = 6
                        TextField {
                            fullWidth = true
                            required = true
                            label = ReactNode("Username")
                            type = InputType.text

                            value = form.username
                            onValueChanged { updateForm { s -> s.copy(username = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 6
                        TextField {
                            fullWidth = true
                            required = true
                            label = ReactNode("Email")
                            type = InputType.email

                            value = form.email
                            onValueChanged { updateForm { s -> s.copy(email = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        UserTagAutocomplete {
                            onTagChange = { tags ->

                            }
                        }
                    }
                    Grid {
                        container = true
                        direction = ResponsiveStyleValue(GridDirection.rowReverse)
                        Grid {
                            item = true
                            xs = "auto"
                            Button {
                                type = ButtonType.submit
                                variant = ButtonVariant.contained
                                +"Add"
                            }
                        }
                    }
                }
            }
        }
    }
}
