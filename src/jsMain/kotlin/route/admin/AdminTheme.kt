package route.admin

import kotlinx.js.jso
import mui.material.PaletteMode
import mui.material.styles.createTheme

val AdminTheme = createTheme(
    jso {
        palette = jso {
            mode = PaletteMode.dark
            primary = jso {
                main = "#e65100"
            }
            secondary = jso {
                main = "#ff9800"
            }
        }
    }
)