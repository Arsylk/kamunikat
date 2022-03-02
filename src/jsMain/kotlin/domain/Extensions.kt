package domain

import model.user.User
import mui.material.TextFieldProps
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import react.Props
import react.PropsWithRef
import react.dom.DOMAttributes
import react.dom.onChange

inline fun TextFieldProps.onValueChanged(crossinline block: (String) -> Unit)  {
    onChange = {
        val raw = (it.target as? HTMLInputElement)?.value.orEmpty()
        block.invoke(raw)
    }
}

inline fun <T: Element> DOMAttributes<T>.onClickEvent(crossinline block: () -> Unit) {
    onClick = {
        it.preventDefault()
        block.invoke()
    }
}

inline fun Props.unsafeOnClickEvent(crossinline block: () -> Unit) {
    unsafeCast<DOMAttributes<*>>().onClickEvent(block)
}

inline val User?.isLoggedIn get() = this != null