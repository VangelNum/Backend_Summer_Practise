package ru.ooo_isk_a_plus.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ooo_isk_a_plus.database.User
import ru.ooo_isk_a_plus.database.UsersTable

fun Application.configureListOfUsers() {
    routing {
        get("/users") {
            val users = transaction {
                UsersTable.selectAll().map {
                    User(
                        phone = it[UsersTable.phone],
                        name = it[UsersTable.name]
                    )
                }
            }
            call.respond(HttpStatusCode.OK, users)
        }
    }
}



