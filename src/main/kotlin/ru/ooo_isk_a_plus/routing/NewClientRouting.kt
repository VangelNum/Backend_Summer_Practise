package ru.ooo_isk_a_plus.routing

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ooo_isk_a_plus.database.UsersTable
import java.util.Base64

fun Application.configureNewUserRouting() {
    routing {
        post("/new_user") {
            try {
                val multipart = call.receiveMultipart()
                var phone = ""
                var name = ""
                var file: String? = null
                var fileName: String? = null
                var fileExtension: String? = null
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "phone" -> phone = part.value
                                "name" -> name = part.value
                            }
                        }

                        is PartData.FileItem -> {
                            val bytes = part.streamProvider().readAllBytes()
                            file = Base64.getEncoder().encodeToString(bytes)
                            fileName = part.originalFileName
                            fileExtension = part.contentType.toString()
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                val isValidPhone = validatePhoneNumber(phone)
                if (!isValidPhone) {
                    call.respond(HttpStatusCode.BadRequest, "Wrong phone number")
                    return@post
                }

                val isPhoneExists = transaction {
                    UsersTable.select { UsersTable.phone eq phone }.count() > 0
                }
                if (isPhoneExists) {
                    call.respond(HttpStatusCode.Conflict, "Phone already exists.")
                    return@post
                }

                transaction {
                    UsersTable.insert {
                        it[UsersTable.name] = name
                        it[UsersTable.phone] = phone
                        it[UsersTable.file] = file
                        it[UsersTable.fileName] = fileName
                        it[UsersTable.fileExtension] = fileExtension
                    }
                }
                call.respond(HttpStatusCode.Created, "User created")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            }
        }
    }
}


fun validatePhoneNumber(phoneNumber: String): Boolean {
    val cleanedPhoneNumber = phoneNumber.replace("\\s".toRegex(), "")
    val regex = Regex("^\\+7\\d{10}$")
    return regex.matches(cleanedPhoneNumber)
}