package component.datatable

import component.datatable.body.DatatableBody
import component.datatable.body.DatatableBodyProps
import component.datatable.footer.DatatableFooter
import component.datatable.header.DatatableHeaderProps
import component.datatable.row.DatatableRowProps
import component.datatable.toolbar.DatatableToolbar
import csstype.*
import domain.base.FCScope
import domain.base.collectAsState
import domain.datatable.DatatableSource
import kotlinx.js.jso
import model.common.Order
import model.common.Sortable
import mui.material.*
import mui.material.Size
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface DatatableProps<Field: Enum<Field>, Item: Any> : Props {
    var isLoading: Boolean?
    var isError: Boolean?
    var throwable: Throwable?

    var page: Int
    var perPage: Int
    var order: Order?
    var orderSelect: Field?
    var fullSize: Int?
    var items: List<Item>

    var title: String?
    var keySelector: (Item) -> Any

    var onAdd: (() -> Unit)?
    var onPageChange: ((Int) -> Unit)?
    var onOrderChange: ((Order?, Field?) -> Unit)?

    var RenderHeader: FC<DatatableHeaderProps<Field>>
    var RenderRow: FC<DatatableRowProps<Item>>
}
val Datatable = FC<DatatableProps<*, *>> { _props ->
    @Suppress("UPPER_BOUND_VIOLATED")
    val props = _props.unsafeCast<DatatableProps<Enum<*>, Any>>()
    Paper {
        sx = jso {
            width = 100.pct
        }

        DatatableToolbar {
            title = props.title
            onAdd = props.onAdd
        }

        Box {
            sx = jso {
                position = Position.relative
            }
            TableContainer {
                Table {
                    sx = jso {
                        size = Size.medium
                    }

                    TableHead {
                        TableRow {
                            props.RenderHeader {
                                order = props.order
                                orderSelect = props.orderSelect
                                onOrderChange = props.onOrderChange
                            }
                        }
                    }

                    @Suppress("UPPER_BOUND_VIOLATED")
                    DatatableBody<DatatableBodyProps<Any>> {
                        isLoading = props.isLoading == true
                        items = props.items
                        rowsPerPage = props.perPage
                        keySelector = props.keySelector

                        RenderRow = props.RenderRow
                    }
                }
            }


            Backdrop {
                sx = jso {
                    position = Position.absolute
                }
                open = props.isLoading == true || props.isError == true
                if (props.isLoading == true) CircularProgress {
                    variant = CircularProgressVariant.indeterminate
                }
                if (props.isError == true) Stack {
                    Typography {
                        component = ReactHTML.h1
                        variant = "h5"
                        +"Error"
                    }
                    props.throwable?.let {
                        Typography {
                            component = ReactHTML.h1
                            variant = "p"
                            +it.stackTraceToString()
                        }
                    }
                }
            }
        }

        DatatableFooter {
            page = props.page
            rowsPerPage = props.perPage
            fullSize = props.fullSize
            onPageChange = props.onPageChange
        }
    }
}


@FCScope
fun <Field: Enum<Field>, Item: Sortable<Field>> DatatableProps<Field, Item>.bindSource(
    source: DatatableSource<Item, Field>,
) {
    val dsState = source.state.collectAsState(source.initialState)
    val operation = source.operation.collectAsState()
    console.log(operation)

    isLoading = operation is DatatableSource.Operation.Loading
    isError = operation is DatatableSource.Operation.Error

    page = dsState.page
    perPage = dsState.perPage
    order = dsState.order
    orderSelect = dsState.orderSelect
    fullSize = dsState.fullSize
    items = dsState.items

    onPageChange = { page ->
        source.setPage(page)
    }
    onOrderChange = { order, field ->
        source.setFullOrder(order, field)
    }
}