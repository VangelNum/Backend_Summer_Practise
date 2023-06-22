package ru.ooo_isk_a_plus

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.jetbrains.exposed.sql.Database
import ru.ooo_isk_a_plus.plugins.configureSerialization
import ru.ooo_isk_a_plus.routing.configureListOfUsers
import ru.ooo_isk_a_plus.routing.configureNewUserRouting
import ru.ooo_isk_a_plus.routing.configureRouting


fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/iskadatabase",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "Kefir13377"
    )
    configureRouting()
    configureListOfUsers()
    configureNewUserRouting()
    configureSerialization()
}


