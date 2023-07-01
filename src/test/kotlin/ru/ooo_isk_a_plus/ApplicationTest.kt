package ru.ooo_isk_a_plus

import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.ktor.util.InternalAPI
import org.jetbrains.exposed.sql.Database
import org.junit.BeforeClass
import org.junit.Test
import ru.ooo_isk_a_plus.plugins.configureSerialization
import ru.ooo_isk_a_plus.routing.configureDeleteUserRouting
import ru.ooo_isk_a_plus.routing.configureListOfUsers
import ru.ooo_isk_a_plus.routing.configureNewUserRouting
import ru.ooo_isk_a_plus.routing.configureRouting
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testListOfUsers() = testApplication {
        application {
            configureSerialization()
            configureListOfUsers()
        }
        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }


    @OptIn(InternalAPI::class)
    @Test
    fun testNewUser() = testApplication {
        application {
            configureNewUserRouting()
        }
        val phoneNumber = "79999999990"
        val name = "John Doe"
        val multipart = MultiPartFormDataContent(
            formData {
                append("phone", phoneNumber)
                append("name", name)
            }
        )
        client.post("/new_user") {
            body = multipart
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertEquals("User created", bodyAsText())
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun testNewUserInvalidPhoneNumber() = testApplication {
        application {
            configureNewUserRouting()
        }
        val name = "John Doe"
        val phoneNumber = "invalid_phone_number"
        val multipart = MultiPartFormDataContent(
            formData {
                append("phone", phoneNumber)
                append("name", name)
            }
        )
        client.post("/new_user") {
            body = multipart
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("Wrong phone number", bodyAsText())
        }
    }


    @Test
    fun testDeleteUser() = testApplication {
        application {
            configureDeleteUserRouting()
        }
        val phoneNumber = "79999999990"
        client.delete("/delete_user?phone=$phoneNumber").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupDatabase() = testApplication {
            application {
                defaultConfiguration()
            }
        }
    }
}

fun defaultConfiguration() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/iskadatabase",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "Kefir13377"
    )
}
