package domain

import component.autocomplete.ApiAutocompleteProps
import model.user.User
import mui.material.TextFieldProps
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import react.*
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

fun <T> StateSetter<T>.update(block: T.() -> T) = invoke(block)

fun <T> StateInstance<T>.update(block: T.() -> T) = component2().invoke(block)

inline val <T> StateInstance<T>.value: T get() = component1()