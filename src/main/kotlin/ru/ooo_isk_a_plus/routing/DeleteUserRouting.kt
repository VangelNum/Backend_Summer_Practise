package ru.ooo_isk_a_plus.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ooo_isk_a_plus.database.UsersTable

fun Application.configureDeleteUserRouting() {
    routing {
        delete("/delete_user") {
            val phone = call.parameters["phone"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Phone number is missing"
            )

            val isDeleted = transaction {
                UsersTable.deleteWhere { UsersTable.phone eq phone } > 0
            }
            if (isDeleted) {
                call.respond(HttpStatusCode.OK, "User deleted")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }
}