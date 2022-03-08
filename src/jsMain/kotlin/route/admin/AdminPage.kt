package route.admin

import csstype.*
import domain.onClickEvent
import domain.unsafeOnClickEvent
import kotlinx.js.jso
import mui.material.*
import mui.material.Size
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.ThemeProvider
import react.*
import react.dom.DOMAttributes
import react.dom.html.ReactHTML
import react.router.Outlet
import react.router.RouteProps
import react.router.useNavigate
import route.Route
import route.admin.users.AdminUsersPageRoute
import mui.icons.material.Menu as MenuIcon

object AdminPageRoute : Route {
    override val path = "/admin"
    override val name = "Admin"

    override fun create(props: RouteProps) = AdminPage.create()
}

val AdminPage = FC<Props> {
//    val user = useRequiredUser()
    ThemeProvider {
        theme = AdminTheme
        Box {
            var isDrawerOpen by useState(true)
            useEffectOnce { isDrawerOpen = true }

            sx = jso { display = Display.flex }
            CssBaseline()

            AppBar {
                val theme = useTheme<Theme>()
                Size.large
                position = AppBarPosition.fixed
                sx = jso {
                    zIndex = ZIndex(theme.zIndex.drawer.toInt() + 1)
                }
                Toolbar {
                    IconButton {
                        size = Size.large
                        edge = IconButtonEdge.start
                        sx = jso { marginRight = 1.rem }
                        onClickEvent { isDrawerOpen = !isDrawerOpen }

                        MenuIcon()
                    }
                    Typography {
                        variant = "h6"
                        noWrap = true
                        component = ReactHTML.div
                    }
                }
            }
            Drawer {
                variant = DrawerVariant.persistent
                open = isDrawerOpen
                sx = jso {
                    flexShrink = FlexShrink(0.0)
                    width = 300.px
                }
                PaperProps = jso {
                    sx = jso {
                        width = 300.px
                        boxSizing = BoxSizing.borderBox
                    }
                }

                Toolbar()
                DrawerContent()
            }
            Box {
                sx = jso { flexGrow = FlexGrow(1.0) }
                Toolbar()
                Container {
                    Outlet()
                }
            }
        }
    }
}

private val DrawerContent = FC<Props> {
    val navigate = useNavigate()
    Box {
        sx = jso { overflow = Overflow.auto }
        List {
            ListItemButton {
                ListItemText {
                    primary = ReactNode("Users")
                    unsafeOnClickEvent { navigate(AdminUsersPageRoute.absolutePath) }
                }
            }
            ListItemButton {
                ListItemText {
                    primary = ReactNode("Permissions")
                }
            }
        }
        Divider()
        List {
            ListItemButton {
                ListItemText {
                    primary = ReactNode("Publications")
                }
            }

            ListItemButton {
                ListItemText {
                    primary = ReactNode("Authors")
//                    unsafeOnClickEvent { navigate(AdminAuthorsPageRoute.absolutePath) }
                }
            }

            ListItemButton {
                ListItemText {
                    primary = ReactNode("Categories")
                }
            }
        }
    }
}