package com.example.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {


    routing {
        static {
            resource("css/st.css","static/css/st.css")
            resource("/","static/index.html")
            resource("*","static/index.html")
        }
    }
}
