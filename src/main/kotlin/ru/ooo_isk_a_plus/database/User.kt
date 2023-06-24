package ru.ooo_isk_a_plus.database

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val phone: String,
    val name: String,
    val file: String? = null
)