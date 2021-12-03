package com.example.plugins

import com.example.entity.Acc
import com.example.entity.AccTable
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

enum class ROLE {
    USER, ADMIN
}

data class UserSession(val name: String, val count: Int, val role: ROLE) : Principal

/*val listAdmin = mapOf<Pair<String,String>,ROLE>(("Eisverlust" to "2222") to ROLE.ADMIN,
    ("User" to "1111") to ROLE.USER)*/

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 1500
        }
    }

    install(Authentication) {
        form("auth-form") {

            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                /* if (listAdmin[credentials.name to credentials.password] !=null) {
                     UserSession(credentials.name,1, listAdmin[credentials.name to credentials.password]!!)
                 } else {
                     null
                 }*/
                newSuspendedTransaction {
                    val acc = Acc.find {
                        AccTable.name eq credentials.name and (AccTable.password eq credentials.password)
                    }.singleOrNull()
                    if (acc != null) {
                        UserSession(credentials.name, 1, ROLE.valueOf(acc.roles.role))
                    } else
                        null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }

        }
        session<UserSession>("admin-session") {
            validate { session ->
                if (session.name.isNotEmpty() && session.role == ROLE.ADMIN) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondHtml(status = HttpStatusCode.Forbidden) {
                    head {
                        link(
                            href = "static/css/login.css"/* javaClass.classLoader.getResource("static/Color.css").toURI().path.drop(1)*/,
                            rel = "stylesheet"
                        )

                    }
                    body {

                        h1 {
                            text("Недостаточно прав")
                        }
                        form(
                            action = "/",
                            encType = FormEncType.applicationXWwwFormUrlEncoded,
                            method = FormMethod.get
                        ) {
                          div ("inputBx") {
                                submitInput() {
                                    value = "Вернутся на главную"

                                }
                            }
                        }
                    }
                }
            }
        }

        session<UserSession>("user-session") {
            validate { session ->
                if (session.name.isNotEmpty() && session.role == ROLE.USER) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }


//--------------------чтобы нахуй не присесть!---------------------------------------------------------------------------------------------
    }
    routing {
        static {
            resource("static/css/login.css", "static/css/login.css")
        }
        get("/login") {
            call.respondHtml {
                head {
                    link(
                        href = "static/css/login.css"/* javaClass.classLoader.getResource("static/Color.css").toURI().path.drop(1)*/,
                        rel = "stylesheet"
                    )

                }

                body {
                    div("section") {
                        div("imgBx") {
                        }
                        div("contentBx") {
                            div("formBx") {
                                h2 {
                                    +"Login"
                                }
                                form(
                                    action = "/login",
                                    encType = FormEncType.applicationXWwwFormUrlEncoded,
                                    method = FormMethod.post
                                ) {
                                    div("inputBx") {
                                        span {
                                            +"Логин"
                                        }
                                        textInput(name = "username") {

                                        }
                                    }
                                    div("inputBx") {
                                        span {
                                            +"Пароль"
                                        }
                                        passwordInput(name = "password") {

                                        }
                                    }
                                    div("remember") {
                                        label {
                                            checkBoxInput {
                                                +"Запомнить меня"
                                            }
                                        }
                                    }
                                    div("inputBx") {
                                        submitInput() {
                                            value = "Войти"

                                        }
                                    }
                                    div("inputBx") {
                                        p {
                                            +"Еще нет аккаунта? "
                                            a {
                                                href = "/registrtion"
                                                +"Создать"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /* form(
                         action = "/login",
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
                             submitInput() { value = "Login" }
                         }
                     }
                     form(
                         action="/registrtion"
                     )
                     {
                         button { + "Registrtion" }*/
                    //}
                }
            }
        }

        authenticate("auth-form") {
            post("/login") {
                val userName = call.principal<UserSession>()
                call.sessions.set(UserSession(name = userName!!.name, count = 1, userName!!.role))
                call.respondRedirect("/")
            }
        }

        authenticate("user-session", "admin-session") {
            get("/hello") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondHtml {
                    body {
                        if (userSession?.role == ROLE.ADMIN) {
                            a {
                                href = "/AdminPanel"
                                +"Admin Menu"
                            }
                        }
                    }
                }
            }
        }
        authenticate("admin-session") {
            get("/admin") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondText("Hello, ${userSession?.name}")
            }
        }
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }

        get("/login"){
            call.respondRedirect("/")
        }
    }
}

