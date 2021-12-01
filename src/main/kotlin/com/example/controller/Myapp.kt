package com.example.controller

import com.example.plugins.UserSession
import com.example.service.AppView
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun Application.myApp() {

    val service = AppView()

    routing {
        static {
            resource("static/css/Color.css","static/css/Color.css")
        }
        authenticate("user-session", "admin-session") {
            get("/back") {
                val id = call.parameters["id"]!!
                service.backApp(id.toInt(),call.principal<UserSession>()!!.name)
                call.respondRedirect("/myapp")
            }
            get("/myapp") {
                val user = call.principal<UserSession>()
                call.respondHtml {
                    head {

                        link(
                            href = "static/css/Color.css"/* javaClass.classLoader.getResource("static/Color.css").toURI().path.drop(1)*/,
                            rel = "stylesheet"
                        )

                    }
                    body {
                        form(
                            action = "/",
                            encType = FormEncType.applicationXWwwFormUrlEncoded,
                            method = FormMethod.get
                        ) {
                            div {
                                submitInput() {
                                    value = "Вернутся на главную"

                                }
                            }
                        }
                    }



                        body(classes = "class") {


                            service.muApplicationList(user!!.name).forEach {
                                div("test") {
                                    div("test2") {
                                        h3 {
                                            +"Заявка ${it.id}"
                                        }
                                        a {
                                            href = "/back?id=${it.id}"
                                            +"Удалить"
                                        }
                                    }
                                    div("hop") {
                                        h4 {
                                            +"Описание"
                                        }
                                        p {
                                            +it.text
                                        }
                                        h4 {
                                            +"адресс"
                                        }
                                        p {
                                            +it.address
                                        }
                                        h4 {
                                            +"Статус"
                                        }
                                        p {
                                            +it.status
                                        }
                                        h4 {
                                            +"Категория"
                                        }
                                        p {
                                            +it.category
                                        }
                                        h4 {
                                            +"Срочно"
                                        }
                                        p {
                                            +it.urgency
                                        }

                                    }
                                    div("dives") {
                                        h3 {
                                            +"Коментарии:"
                                        }
                                        it.listComment?.forEach {
                                            p {
                                                +it
                                            }
                                        }
                                    }
                                }
                            }

                        }

                }
            }
        }
    }
}