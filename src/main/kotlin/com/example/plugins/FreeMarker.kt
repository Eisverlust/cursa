package com.example.plugins

import com.example.BlogEntry
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

enum class Test{
    AAA
}

fun Application.freeMarker() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
    }
    routing {
        val blogEntries = mutableListOf(BlogEntry(
                "Cat gerls!",
                "Lolikon"
            )
        )


    }
}