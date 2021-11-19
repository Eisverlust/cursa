package com.example.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*

enum class ROLE{
    USER,ADMIN
}

data class UserSession(val name: String, val count: Int,val role:ROLE) : Principal

val listAdmin = mapOf<Pair<String,String>,ROLE>(("IceToll" to "3242") to ROLE.ADMIN,
    ("User" to "1111") to ROLE.USER)

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 150000
        }
    }

    install(Authentication) {
        form("auth-form") {

            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (listAdmin[credentials.name to credentials.password] !=null) {
                    UserSession(credentials.name,1, listAdmin[credentials.name to credentials.password]!!)
                } else {
                    null
                }
            }
        }
        session<UserSession>("user-session") {
            validate { session ->
                if(session.name.isNotEmpty()) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }

        session<UserSession>("admin-session") {
            validate { session ->
                if(session.name.isNotEmpty() && session.role ==ROLE.ADMIN) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondHtml(status = HttpStatusCode.Forbidden) {
                    body {
                        h1 {
                            text("Иди на ...")
                        }
                        button() {
                            "Дя"
                        }
                    }
                }
            }
        }
    }
//--------------------чтобы нахуй не присесть!---------------------------------------------------------------------------------------------

    routing {
        get("/login") {
            call.respondHtml {
                body {
                    form(action = "/login", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                        p {
                            +"Username:"
                            textInput(name = "username")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput() { value = "Login" }
                        }
                    }
                }
            }
        }

        authenticate("auth-form") {
            post("/login") {
                val userName = call.principal<UserSession>()
                call.sessions.set(UserSession(name = userName!!.name, count = 1, userName!!.role))
                call.respondRedirect("/hello")
            }
        }

        authenticate("user-session","admin-session") {
            get("/hello") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondText("Hello, ${userSession?.name}! Visit count is ${userSession?.count}.")
            }
        }
        authenticate("admin-session") {
            get("/admin") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondText("Hello, ${userSession?.name} Admin.")
            }
        }
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }
}
