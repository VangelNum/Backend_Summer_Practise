package ru.ooo_isk_a_plus

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import org.junit.Test
import ru.ooo_isk_a_plus.routing.configureNewUserRouting

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureNewUserRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
