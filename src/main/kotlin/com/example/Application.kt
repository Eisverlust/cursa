package com.example

import com.example.controller.addAppController
import com.example.controller.adminPanel
import com.example.controller.myApp
import com.example.controller.registerRouting
import com.example.entity.*
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.freeMarker
import com.example.service.BotService
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        Database.connect(
            "jdbc:postgresql://localhost:5432/CurS",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = System.getenv("Pass")// ахахаххах вы хотели мой пароль , обайдетесь :D
        )
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccTable,
                RolesTable,
                ApplicationUserTable,
                ContactTable,
                GroupTable,
                CategoryTable,
                EmpTable,
                UrgencyTable,
                CommentTable,
                MagazineTable,
                StatusAppTable,
            )
        }
        configureRouting()
        configureSecurity()
        freeMarker()
        registerRouting()
        adminPanel()
        myApp()
        routing {
            addAppController()
        }
        BotService.start()
    }.start(wait = true)
}
