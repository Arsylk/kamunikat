package domain.common

import domain.koin.get
import kotlinx.browser.localStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalStorageDelegate<T: Any>(
    val key: String,
    val mapper: Mapper<T>,
) {

    interface Mapper<T: Any> {
        fun serialize(value: T): String
        fun deserialize(string: String): T
    }

    companion object {
        val StringMapper = object : Mapper<String> {
            override fun serialize(value: String) = value
            override fun deserialize(string: String) = string
        }
    }
}


operator fun <T: Any> LocalStorageDelegate<T>.getValue(thisRef: Any, any: Any): T? {
    val raw = localStorage.getItem(key)
    if (raw != null) try {
        return mapper.deserialize(raw)
    }catch (_: Throwable) {
    }
    return null
}

operator fun <T: Any> LocalStorageDelegate<T>.setValue(thisRef: Any, any: Any, value: T?) {
    if (value != null) {
        val mapped = mapper.serialize(value)
        localStorage.setItem(key, mapped)
    } else {
        localStorage.removeItem(key)
    }
}

inline fun <reified T: Any> localStorage(key: String): LocalStorageDelegate<T> {
    val json = get<Json>()
    val mapper = object : LocalStorageDelegate.Mapper<T> {
        override fun serialize(value: T) = json.encodeToString(value)
        override fun deserialize(string: String): T = json.decodeFromString(string)
    }
    return localStorage(key, mapper)
}

fun localStorage(key: String): LocalStorageDelegate<String> {
    return localStorage(key, LocalStorageDelegate.StringMapper)
}

inline fun <reified T: Any> localStorage(
    key: String,
    mapper: LocalStorageDelegate.Mapper<T>,
) = LocalStorageDelegate(key, mapper)