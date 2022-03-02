package route.admin

import domain.auth.AuthService
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Routing.adminController() {
    authenticate(AuthService.SessionAuthName) {
        static("/admin/{...}") {
            defaultResource("index.html")
        }
    }
}