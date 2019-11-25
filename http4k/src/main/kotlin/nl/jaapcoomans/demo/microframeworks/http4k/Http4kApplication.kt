package nl.jaapcoomans.demo.microframeworks.http4k

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val app = routes(
            "/hello" bind GET to { Response(OK).body("Hello World!") },
            "/hello/{name}" bind GET to helloController
    )

    val server = app.asServer(Jetty(8080))

    server.start()
}

val helloController = fun(req: Request): Response {
    return Response(OK).body("Hello, ${req.path("name")?.capitalize()}!")
}