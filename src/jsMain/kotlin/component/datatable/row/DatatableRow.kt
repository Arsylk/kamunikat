package component.datatable.row

import react.PropsWithChildren

external interface DatatableRowProps<T: Any> : PropsWithChildren {
    var position: Int
    var item: T
}