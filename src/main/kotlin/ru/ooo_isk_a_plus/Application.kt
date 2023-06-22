package ru.ooo_isk_a_plus

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.jetbrains.exposed.sql.Database
import ru.ooo_isk_a_plus.routing.configureListOfUsers
import ru.ooo_isk_a_plus.routing.configureNewUserRouting
import ru.ooo_isk_a_plus.plugins.configureSerialization
import java.io.FileInputStream
import java.util.Properties

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val config = Properties()
    val configFile = FileInputStream("config.properties")
    config.load(configFile)
    configFile.close()

    val dbUrl = config.getProperty("db.url")
    val dbDriver = config.getProperty("db.driver")
    val dbUser = config.getProperty("db.user")
    val dbPassword = config.getProperty("db.password")

    Database.connect(
        url = dbUrl,
        driver = dbDriver,
        user = dbUser,
        password = dbPassword
    )

    configureListOfUsers()
    configureNewUserRouting()
    configureSerialization()
}

