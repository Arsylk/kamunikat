package route

import domain.base.FCScope
import react.*
import react.router.RouteProps

interface Route {
    val path: String
    val name: String
    val parent: Route? get() = null

    val absolutePath: String get() {
        var full = path

        var next = parent
        while (next != null) {
            full = if (next.path != "/") "${next.path}/$full" else "/$full"
            next = next.parent
        }
        return full
    }

    fun create(props: RouteProps): ReactNode
}

fun ChildrenBuilder.Route(
    route: Route,
    block: @ReactDsl ChildrenBuilder.() -> Unit = {},
) {
    react.router.Route {
        path = route.path
        element = route.create(this)
        block(this)
    }
}