package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.application.*
import io.ktor.auth.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

        configureRouting()
        configureSecurity()
        freeMarker()
    }.start(wait = true)
}
