package model.common

enum class Gender(val text: String) {
    Male("m"), Female("f"), Unknown("");

    companion object {
        fun mapFrom(string: String?) = values()
            .firstOrNull { it.text.equals(string, ignoreCase = true) }
            ?: Unknown
    }
}