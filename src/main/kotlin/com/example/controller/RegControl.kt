package com.example.controller

import com.example.service.Registration
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.html.*

fun Application.registerRouting(){

    val service = Registration()

    routing {
        get("/registrtion") {
            call.respondHtml {
                body {
                    form(
                        action = "/registrtion",
                        encType = FormEncType.applicationXWwwFormUrlEncoded,
                        method = FormMethod.post
                    ) {
                        p {
                            +"Username:"
                            textInput(name = "username")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput() { value = "Registration" }
                        }
                    }
                }
            }
        }
        post("/registrtion") {
            val microDto = call.receiveParameters()
            service.registration(microDto["username"].toString(),microDto["password"].toString())
            println(microDto)
        }
    }
}