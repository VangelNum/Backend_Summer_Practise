package ru.ooo_isk_a_plus

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import ru.ooo_isk_a_plus.plugins.configureSerialization
import ru.ooo_isk_a_plus.routing.configureListOfUsers
import ru.ooo_isk_a_plus.routing.configureNewUserRouting
import ru.ooo_isk_a_plus.routing.configureRouting


fun main() {
    embeddedServer(
        Netty,
        port = 6654,
        host = "containers-us-west-188.railway.app",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    Database.connect(
        url = "jdbc:postgresql://postgres:VbBpucXWGDV201X7odWY@containers-us-west-188.railway.app:6654/railway",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "VbBpucXWGDV201X7odWY"
    )
    configureRouting()
    configureListOfUsers()
    configureNewUserRouting()
    configureSerialization()
}


