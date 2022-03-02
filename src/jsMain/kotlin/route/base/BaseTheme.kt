package route.base

import kotlinx.js.jso
import mui.material.styles.createTheme

val BaseTheme = createTheme(
    jso {
        palette = jso {
            mode = "light"
        }
    }
)