package route.admin.authors


//object AdminAuthorsPageRoute : Route {
//    override val path = "authors"
//    override val name = "Authors"
//    override val parent = AdminPageRoute
//}
//
//val columns = listOf(
//    DatatableHeaderColumn(
//        key = DatatableColumnKey(1),
//        text = "Id",
//        colSpan = 2,
//        sortable = true,
//        align = TableCellAlign.left
//    ),
//    DatatableHeaderColumn(
//        key = DatatableColumnKey(2),
//        text = "Name",
//        colSpan = 1,
//        sortable = true,
//        align = TableCellAlign.left
//    ),
//    DatatableHeaderColumn(
//        key = DatatableColumnKey(3),
//        text = "Timestamp",
//        colSpan = 1,
//        sortable = true,
//        align = TableCellAlign.left
//    )
//)
//
//
//@Suppress("UPPER_BOUND_VIOLATED")
//val cells = listOf<DatatableCell<DTItem>>(
//    DatatableCell(
//        key = DatatableCellKey(-1),
//        fc = FC {
//            TableCell {
//                padding = TableCellPadding.checkbox
//                Checkbox {
//                    color = CheckboxColor.primary
//                }
//            }
//        }
//    ),
//    DatatableCell(
//        key = DatatableCellKey(1),
//        fc = FC { props ->
//            SimpleDatatableCell {
//                text = props.item.key.toString()
//                onClick = props.onCellClick
//            }
//        }
//    ),
//    DatatableCell(
//        key = DatatableCellKey(2),
//        fc = FC { props ->
//            SimpleDatatableCell {
//                text = props.item.name
//                onClick = props.onCellClick
//            }
//        }
//    ),
//    DatatableCell(
//        key = DatatableCellKey(2),
//        fc = FC { props ->
//            SimpleDatatableCell {
//                text = props.item.timestamp.toString()
//                onClick = props.onCellClick
//            }
//        }
//    )
//)
//
//
//data class DTItem(
//    override val key: Int,
//    val name: String,
//    val timestamp: Instant
//) : DatatableItem
////
////val fakeList = DatatableList(
////    currentItems = (0 until 15).map { i ->
////        DTItem(key = i, name = "name for $i", timestamp = Clock.System.now() + i.days)
////    },
////    fullSize = 48,
////    page = 1,
////    rowsPerPage = 15,
////)
//
//val AdminAuthorsPage = FC<Props> {
//    val navigate = useNavigate()
//    val viewModel by useViewModel<AdminAuthorsViewModel>()
//
//    val list = viewModel.authors.collectAsState(emptyList())
//    val orderBy = viewModel.orderBy.collectAsState()
//
//    var order by useState<DatatableSortOrder?>(null)
//    @Suppress("UPPER_BOUND_VIOLATED")
//    Datatable<DatatableProps<DTItem>> {
//        title = "DT Item"
////        this.list = fakeList
//        headerColumns = columns
//        rowCells = cells
//        sortOrder = order
//
//        onAdd = {
//            console.log("on add !")
//        }
//        onSortOrderChange = { newOrder ->
//            console.log("on sort order change: $newOrder")
//            order = newOrder
//        }
//        onPageChange = { page ->
//            console.log("on page change: $page")
//        }
//        onCellClick = { row, cell ->
//            console.log("on click: row: $row, cell: $cell")
//        }
//    }
//}