package route

interface Route {
    val path: String
    val name: String
    val parent: Route? get() = null

    val absolutePath: String get() {
        var full = path

        var next = parent
        while (next != null) {
            full = if (next.path != "/") "${next.path}/$full" else "/$full"
            next = next.parent
        }
        return full
    }
}