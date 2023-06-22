package ru.ooo_isk_a_plus.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ooo_isk_a_plus.database.User
import ru.ooo_isk_a_plus.database.UsersTable

fun Application.configureNewUserRouting() {
    routing {
        post("/new_user") {
            try {
                val newUser = call.receive<User>()

                val isValidPhone = validatePhone(newUser.phone)
                if (!isValidPhone) {
                    call.respond(HttpStatusCode.BadRequest, "Wrong phone number")
                    return@post
                }

                val isPhoneExists = transaction {
                    UsersTable.select { UsersTable.phone eq newUser.phone }.count() > 0
                }
                if (isPhoneExists) {
                    call.respond(HttpStatusCode.Conflict, "Phone already exists.")
                    return@post
                }

                transaction {
                    UsersTable.insert {
                        it[name] = newUser.name
                        it[phone] = newUser.phone
                    }
                }
                call.respond(HttpStatusCode.Created, "User created")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            }
        }
    }
}


fun validatePhone(phoneNumber: String): Boolean {
    val regex = Regex("^7\\d{10}$")
    return regex.matches(phoneNumber)
}