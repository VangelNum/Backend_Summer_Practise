package ru.ooo_isk_a_plus

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import ru.ooo_isk_a_plus.plugins.configureSerialization
import ru.ooo_isk_a_plus.routing.configureDeleteUserRouting
import ru.ooo_isk_a_plus.routing.configureListOfUsers
import ru.ooo_isk_a_plus.routing.configureNewUserRouting


fun main() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://containers-us-west-188.railway.app:6654/railway"
        username = "postgres"
        password = "VbBpucXWGDV201X7odWY"
        driverClassName = "org.postgresql.Driver"
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
//    Database.connect(
//        url = "jdbc:postgresql://localhost:5432/iskadatabase",
//        driver = "org.postgresql.Driver",
//        user = "postgres",
//        password = "Kefir13377"
//    )
    embeddedServer(Netty, host = "0.0.0.0", port = 8080) {
        configureListOfUsers()
        configureDeleteUserRouting()
        configureNewUserRouting()
        configureSerialization()
    }.start(wait = true)
}


