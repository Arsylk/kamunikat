package route.base

import io.ktor.http.content.*
import io.ktor.routing.*

fun Routing.baseController() {
    static("/{...}") {
        defaultResource("index.html")
    }
}