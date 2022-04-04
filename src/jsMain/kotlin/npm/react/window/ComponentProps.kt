@file:JsModule("react-window")
@file:JsNonModule
package npm.react.window

import csstype.*
import react.*

external interface ListChildComponentProps<T: Any> : PropsWithStyle {
    var index: Int
    var data: T
}

external interface CommonProps<T: Any> : PropsWithStyle {
    var className: String?
    var innerElementType: dynamic
    var innerRef: Ref<*>?
    var itemData: T?

    var outerElementType: dynamic
    var outerRef: Ref<*>?

    var useIsScrolling: Boolean?
}

external interface ListOnItemsRenderedProps : Props {
    var overscanStartIndex: Int
    var overscanStopIndex: Int
    var visibleStartIndex: Int
    var visibleStopIndex: Int
}

external interface ListOnScrollProps : Props {
    var scrollDirection: String /* "forward" | "backward" */
    var scrollOffset: NumberType
    var scrollUpdateWasRequested: Boolean
}

external interface ListProps<T: Any> : CommonProps<T>, PropsWithChildren {
    var height: NumberType
    var width: Length
    var itemCount: Int
    var direction: Direction?
    var initialScrollOffset: NumberType?
    var itemKey: ((index: Int) -> Key)?
    var overscanCount: Int?
    var onItemsRendered: ((ListOnItemsRenderedProps) -> Unit)?
    var onScroll: ((ListOnScrollProps) -> Unit)?
}

external interface VariableSizeListProps<T: Any> : ListProps<T> {
    var estimatedItemSize: NumberType?
    var itemSize: (index: Int) -> NumberType
}