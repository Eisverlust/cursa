package com.example

import com.example.controller.registerRouting
import com.example.entity.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.application.*
import io.ktor.auth.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        Database.connect("jdbc:postgresql://localhost:5432/CurS", driver = "org.postgresql.Driver", user = "postgres", password = "IceToll_1")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(AccTable,RolesTable,ApplictionUserTable,ContactTable,GroupTable,CategoryTable,EmpTable,StatusTable)
        }
        configureRouting()
        configureSecurity()
        freeMarker()
        registerRouting()
    }.start(wait = true)
}
