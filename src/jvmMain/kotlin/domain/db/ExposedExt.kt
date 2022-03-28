package domain.db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet


fun <T: Any> Database.rawExec(query: String, transform : (ResultSet) -> T) : List<T> {
    val result = arrayListOf<T>()
    transaction(this) {
        exec(query) { rs ->
            while (rs.next()) {
                result += transform(rs)
            }
        }
    }
    return result
}

fun <T: Any> Database.rawSelect(query: String, transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    transaction(this) {
        val set = connection.prepareStatement(query, false).executeQuery()
        while (set.next()) {
            result += transform(set)
        }
    }
    return result
}

inline val <T: IntEntity> T.idInt: Int get() = id.value

fun EntityClass<*, *>.countInt() = count().toInt()

fun <T : Comparable<T>, R : Entity<T>> EntityClass<T, R>.new(
    db: Database,
    init: R.() -> Unit,
) = new(db, null, init)

fun <T : Comparable<T>, R : Entity<T>> EntityClass<T, R>.new(
    db: Database,
    id: T?,
    init: R.() -> Unit,
) = transaction(db) { new(id, init) }