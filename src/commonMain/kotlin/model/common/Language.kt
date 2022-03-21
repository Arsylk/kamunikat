package model.common

enum class Language(val text: String) {
    English("en"), Polish("pl"), Belarusian("be");

    companion object {
        fun mapFrom(string: String) = values()
            .firstOrNull { it.text.equals(string, ignoreCase = true) }
            ?: Belarusian
    }
}