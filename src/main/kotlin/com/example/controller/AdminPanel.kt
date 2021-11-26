package com.example.controller

import com.example.service.AdminService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun Application.adminPanel() {

    val service = AdminService()

    routing {
        static {
            resource("static/css/Color.css","static/css/Color.css")
        }
        authenticate("admin-session") {
            get("remove") {
                val id = call.parameters["id"]
                service.removeEmployer(id!!.toInt())
                call.respondRedirect("/AdminPanel")
            }
            get("/AdminPanel") {
                val countApp = service.countApplication()
                val countExecuteApp = service.countExecuteApp()
                val percent = if (countApp != 0.0 && countExecuteApp != 0.0)
                        ( countExecuteApp/countApp) * 100
                else if (countApp != 0.0)
                    0
                else 100
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
                            div ("inputBx") {
                                submitInput() {
                                    value = "Вернутся на главную"

                                }
                            }
                        }
                        h2 {
                            +"Соотношение работы"
                        }
                        p {
                            +"количество обработанных заявок"
                        }
                        p {
                            +"${percent}%"

                        }
                        p {
                            +"завершено/всего "
                        }
                        p {
                            +"$countExecuteApp/$countApp"
                        }

                        h2 {
                            +"Сотрудники:"
                        }
                        div(classes = "class") {
                            service.listEmployer().forEach {
                                div("test") {
                                    a {
                                        href = "/remove?id=${it.id}"
                                        +"удалить"
                                    }
                                    p{
                                        +"выполнено ${((service.empPercent(it.id))/countApp) *100}%"
                                    }
                                    p {
                                        +it.fio
                                    }
                                    p {
                                        +it.uuid
                                    }
                                    p {
                                        +it.group
                                    }
                                    div("hop") {
                                        h4 {
                                            +"Контакты"
                                        }
                                        it.phones.forEach { p ->
                                            p {
                                                +p
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
}