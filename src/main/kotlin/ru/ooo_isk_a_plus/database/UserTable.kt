package ru.ooo_isk_a_plus.database

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val phone = text("phone")
    val name = text("name")
    val file = text("file").nullable()
    val fileExtension = text("fileExtension").nullable()
}

