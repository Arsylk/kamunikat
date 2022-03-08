package component.datatable.footer

import mui.material.TablePagination
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface DatatableFooterProps : Props {
    var page: Int
    var rowsPerPage: Int
    var fullSize: Int?
    var onPageChange: ((Int) -> Unit)?
}
val DatatableFooter = FC<DatatableFooterProps> { props ->
    TablePagination {
        component = ReactHTML.div

        page = props.page
        rowsPerPage = props.rowsPerPage
        count = props.fullSize ?: -1
        rowsPerPageOptions = arrayOf(props.rowsPerPage)

        onPageChange = { _, page ->
            props.onPageChange?.invoke(page.toInt())
        }
    }
}