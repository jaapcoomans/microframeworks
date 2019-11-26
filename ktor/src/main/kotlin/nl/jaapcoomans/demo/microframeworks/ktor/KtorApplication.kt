package nl.jaapcoomans.demo.microframeworks.ktor

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val server = embeddedServer(Netty, 8080) {
        routing {
            get("/hello") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/hello/{name}") {
                call.respondText("Hello, ${call.parameters["name"]?.capitalize()}!", ContentType.Text.Plain)
            }
        }
    }

    server.start(wait = true)
}