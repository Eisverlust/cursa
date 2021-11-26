package com.example.controller

import com.example.service.Registration
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun Application.registerRouting(){

    val service = Registration()


    routing {
        static {
            resource("static/css/Color.css","static/css/Color.css")
        }
        get("/registrtion") {
            call.respondHtml {
                head {
                    link(
                        href ="static/css/Color.css"/* javaClass.classLoader.getResource("static/Color.css").toURI().path.drop(1)*/,
                        rel = "stylesheet"
                    )

                }
                body(classes = "class") {
                    form(
                        action = "/registrtion",
                        encType = FormEncType.applicationXWwwFormUrlEncoded,
                        method = FormMethod.post
                    ) {
                        p(classes = "class") {
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
            call.respondRedirect("/login")
        }
    }
}
