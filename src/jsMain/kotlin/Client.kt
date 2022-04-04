import domain.koin.setupKoin
import kotlinx.browser.document
import kotlinx.browser.window
import org.koin.core.context.startKoin
import react.create
import react.dom.client.createRoot
import react.dom.render


fun main() {
    startKoin { setupKoin() }
    window.onload = {
        val container = document.getElementById("root")
        val root = createRoot(container!!)
        root.render(App.create())
    }
}