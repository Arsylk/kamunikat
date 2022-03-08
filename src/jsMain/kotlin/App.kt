import domain.auth.provideUser
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.h1
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import route.Route
import route.admin.AdminPageRoute
import route.admin.users.AdminUserAddPageRoute
import route.admin.users.AdminUsersPageRoute
import route.base.BasePage
import route.base.home.HomePage
import route.base.home.HomePageRoute
import route.base.login.LoginPageRoute


val App = FC<Props> {
    provideUser {
        BrowserRouter {
            Routes {
                Route {
                    path = HomePageRoute.path
                    element = BasePage.create()

                    Route {
                        index = true
                        element = HomePage.create()
                    }

                    Route(LoginPageRoute)

                    Route {
                        path = "*"
                        element = FC<Props> { h1 { +"Page not found" } }.create()
                    }
                }
                Route(AdminPageRoute) {
                    Route(AdminUsersPageRoute)
                    Route(AdminUserAddPageRoute)
                }
            }
        }
    }
}
//        var showDialog by useState(false)
//        var authors by useState<Array<Author>?>(null)
//
//        var text by useState("Loading")
//        useEffectOnce {
//            scope.launch {
//                val jsonClient: HttpClient by inject()
//                val data: List<Author> = jsonClient.get("$endpoint/api/authors")
//                authors = data.toTypedArray()
//            }
//        }
//
//        AdminPage().render()
//
//        @Suppress("UPPER_BOUND_VIOLATED")
//        Grid {
//            container = true
//            spacing = ResponsiveStyleValue(1)
//            css { alignItems = AlignItems.center }
//
//            Grid {
//                item = true
//                xs = true
//                Autocomplete<AutocompleteProps<Author>> {
//                    options = authors.orEmpty()
//                    freeSolo = true
//                    multiple = true
//                    clearOnBlur = true
//                    loading = authors == null
//
//                    filterOptions = { list, filter ->
//                        val input = filter.inputValue
//                        val filtered = list
//                            .filter {
//                                it.name.contains(input, ignoreCase = true) ||
//                                        it.content.contains(input, ignoreCase = true)
//                            }
//                        filtered.toTypedArray()
//                    }
//                    getOptionLabel = { item -> item.name }
//                    renderInput = { params ->
//                        TextField.create {
//                            +params
//                            label = ReactNode("Authors")
//                        }
//                    }
//                    renderOption = { props, item, state ->
//                        li.create {
//                            +props
//                            +"$item"
//                        }
//                    }
//                    onChange = { event, newValue, reason, details ->
//                        println(JSON.stringify(newValue))
//                        if (newValue is String) {
//                            showDialog = true
//                        }
//                    }
//                }
//            }
//            Grid {
//                item = true
//                IconButton {
//                    onClick = { showDialog = true }
//                    size = Size.large
//                    AddCircle()
//                }
//            }
//        }
//
//
//        Dialog {
//            open = showDialog
//            onClose = { _, _ -> showDialog = false }
//            form {
//                onSubmit = { event ->
//                    event.preventDefault()
//                    println(JSON.stringify(event.nativeEvent.target))
//                }
//
//                DialogTitle { +"Add author" }
//                DialogContent {
//                    DialogContentText {
//                        +"Text"
//                    }
//                    Grid {
//                        container = true
//                        spacing = ResponsiveStyleValue(1)
//                        direction = ResponsiveStyleValue(GridDirection.column)
//                        Grid {
//                            container = true
//                            spacing = ResponsiveStyleValue(1)
//                            direction = ResponsiveStyleValue(GridDirection.row)
//                            Grid {
//                                item = true
//                                xs = 6
//                                TextField {
//                                    label = ReactNode("Id")
//                                    variant = FormControlVariant.outlined
//                                    type = InputType.number
//                                    name = "id"
//                                }
//                            }
//                            Grid {
//                                item = true
//                                xs = 6
//                                TextField {
//                                    label = ReactNode("Name")
//                                    variant = FormControlVariant.outlined
//                                    type = InputType.text
//                                    name = "name"
//                                }
//                            }
//                        }
//                        Grid {
//                            item = true
//                            xs = true
//                            TextField {
//                                label = ReactNode("Content")
//                                variant = FormControlVariant.outlined
//                                type = InputType.text
//                                name = "content"
//                            }
//                        }
//                    }
//                }
//                DialogActions {
//                    Button {
//                        +"Cancel"
//                        onClick = { showDialog = false }
//                    }
//                    Button {
//                        +"Add"
//                        type = ButtonType.submit
//                    }
//                }
//            }
//        }
