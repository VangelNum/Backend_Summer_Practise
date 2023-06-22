package ru.ooo_isk_a_plus.database

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val phone = varchar("phone", 25)
    val name = varchar("name", 25)
}
