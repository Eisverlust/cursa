package com.example.controller

import com.example.entity.Category
import com.example.entity.Urgency
import com.example.plugins.UserSession
import com.example.service.AppAdd
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun Routing.addAppController() {

    val service = AppAdd()

    route("app") {
        static {
            resource("static/css/Color.css","static/css/Color.css")
        }
        authenticate("user-session", "admin-session") {
            post("add") {
                val microDto2 = call.receiveParameters()
                println(microDto2.toString())
                service.addApp(
                    text = microDto2["Text"].toString(),
                    fio = call.principal<UserSession>()!!.name,
                    phone = microDto2["phone"].toString(),
                    LocalDateTime.now(),
                    address = microDto2["address"].toString(),
                    "На обработке",
                    microDto2["category"].toString(),
                    microDto2["urgency"].toString()
                )
                call.respondRedirect("/")
            }
            get("add") {

                call.respondHtml {

                    head {
                        link(
                            href = "static/css/Color.css"/* javaClass.classLoader.getResource("static/Color.css").toURI().path.drop(1)*/,
                            rel = "stylesheet"
                        )

                    }
                    body(classes = "class") {
                        form(
                            action = "/app/add",
                            encType = FormEncType.applicationXWwwFormUrlEncoded,
                            method = FormMethod.post
                        ) {
                            p(classes = "class") {
                                +"Text:"
                                textInput(name = "Text") {
                                    pattern = ".{1,}"
                                    this.required = true
                                }
                            }
                            p {
                                +"address"
                                textInput(name = "address") {
                                    pattern = ".{1,}"
                                    this.required = true
                                }
                            }
                            p{
                                +"phone"
                                textInput(name = "phone") {
                                    //Нужно добавить патерн
                                }
                            }
                            select() {
                                name = "category"
                                categories().map {
                                    option {
                                        +it.category
                                    }
                                }
                            }
                            select {
                                name = "urgency"
                                urga().map {
                                    option {
                                        +it.urgency
                                    }
                                }
                            }
                            submitInput {}
                        }
                    }
                }
            }
        }
    }
}


fun categories(): List<Category> {
    return transaction {
        return@transaction Category.all().map { it }
    }
}

fun urga(): List<Urgency> {
    return transaction {
        return@transaction Urgency.all().map { it }
    }
}