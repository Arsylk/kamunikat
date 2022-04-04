@file:JsModule("react-window")
@file:JsNonModule
package npm.react.window
import csstype.AlignContent
import react.ComponentClass


external interface VariableSizeListClass<T: Any> : ComponentClass<VariableSizeListProps<T>> {
    fun scrollTo(scrollOffset: Number)
    fun scrollToItem(index: Int, align: String /*center | end | start */)
    fun resetAfterIndex(index: Int, shouldForceUpdate: Boolean?)
}

@JsName("VariableSizeList")
external val VariableSizeList: VariableSizeListClass<Any>