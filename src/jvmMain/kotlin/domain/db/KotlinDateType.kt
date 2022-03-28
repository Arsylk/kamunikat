package domain.db

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toKotlinLocalDate
import model.db.user.User.Companion.transform
import org.jetbrains.exposed.sql.Column
import java.time.Instant
import java.time.LocalDate

fun Column<Instant>.kInstant() = transform(
    { k -> k.toJavaInstant() },
    { j -> j.toKotlinInstant() },
)

@JvmName("kDateNullable")
fun Column<LocalDate?>.kDate() = transform(
    { k -> k?.toJavaLocalDate() },
    { j -> j?.toKotlinLocalDate() },
)

fun Column<LocalDate>.kDate() = transform(
    { k -> k.toJavaLocalDate() },
    { j -> j.toKotlinLocalDate() },
)