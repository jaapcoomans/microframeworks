package nl.jaapcoomans.demo.microframeworks.http4k

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.PATCH
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.AllowAllOriginPolicy
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.util.Locale

fun main() {
    val todoRepository = InMemoryTodoRepository()
    val todoService = TodoService(todoRepository)
    val todoRestController = TodoRestController(todoService)

    val app = routes(
            "/hello" bind GET to { Response(OK).body("Hello World!") },
            "/hello/{name}" bind GET to helloController,
            "/todos" bind GET to todoRestController::getAll,
            "/todos" bind POST to todoRestController::createTodo,
            "/todos" bind DELETE to todoRestController::deleteAll,
            "/todos/{id}" bind GET to todoRestController::getTodo,
            "/todos/{id}" bind PATCH to todoRestController::patchTodo,
            "/todos/{id}" bind DELETE to todoRestController::deleteTodo
    ).withFilter(ServerFilters.Cors(CorsPolicy(
            originPolicy = AllowAllOriginPolicy,
            headers = listOf("Content-Type"),
            methods = listOf(GET, POST, DELETE, PATCH)
    )))

    val server = app.asServer(Jetty(8080))

    server.start()
}

val helloController = fun(req: Request): Response {
    return Response(OK).body("Hello, ${req.path("name")?.capitalize()}!")
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

