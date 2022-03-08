package route.base.home

import mui.material.Container
import mui.material.Typography
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.html.ReactHTML
import react.router.RouteProps
import route.Route

object HomePageRoute : Route {
    override val path = "/"
    override val name = "Home"

    override fun create(props: RouteProps) = HomePage.create()
}

val HomePage = FC<Props> {
    Container {
        component = ReactHTML.main
        Typography {
            variant = "h3"
            component = ReactHTML.div
            +"Home Page"
        }
    }
}