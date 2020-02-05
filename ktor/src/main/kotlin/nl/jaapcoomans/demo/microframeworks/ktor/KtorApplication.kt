package nl.jaapcoomans.demo.microframeworks.ktor

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.options
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val startTime = System.currentTimeMillis()

    install(ContentNegotiation) {
        jackson {}
    }
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        method(HttpMethod.Options)
        anyHost()
        allowNonSimpleContentTypes = true
        allowSameOrigin = false
    }

    routing {
        get("/hello") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        get("/hello/{name}") {
            call.respondText("Hello, ${call.parameters["name"]?.capitalize()}!", ContentType.Text.Plain)
        }

        todoRoutes()
    }

    environment.monitor.subscribe(ApplicationStarted) {
        val bootTime = System.currentTimeMillis() - startTime
        log.info("Started in ${bootTime} ms.");
    }
}