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