package domain

import model.user.User
import mui.material.TextFieldProps
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.MutableRefObject
import react.Props
import react.StateInstance
import react.StateSetter
import react.dom.DOMAttributes
import react.dom.onChange

inline fun TextFieldProps.onValueChanged(crossinline block: (String) -> Unit)  {
    onChange = {
        val raw = when (val target = it.target) {
            is HTMLInputElement -> target.value
            is HTMLTextAreaElement -> target.value
            else -> null
        }.orEmpty()
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

fun <T> StateSetter<T>.update(block: T.() -> T) = invoke(block)

fun <T> StateInstance<T>.update(block: T.() -> T) = component2().invoke(block)

inline val <T> StateInstance<T>.value: T get() = component1()

fun <T : Any> MutableRefObject<T>.update(block: T.() -> T) {
    current = current?.run(block)
}