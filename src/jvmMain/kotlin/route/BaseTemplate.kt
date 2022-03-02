package route.base

import io.ktor.html.*
import kotlinx.html.*

class BaseTemplate : Template<HTML> {
    val content = Placeholder<FlowContent>()
    override fun HTML.apply() {
        head {
            title("Hello from Ktor!")

            link {
                rel = "stylesheet"
                href = "https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"
            }
            script {
                src = "/static/js/kamunikat.js"
            }
        }
        body {
            insert(content)
            div {
                id = "root"
            }
            HTMLTag("TestInput", consumer, emptyMap, null, false, false).visit {}
        }
    }
}