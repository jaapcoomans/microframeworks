package nl.jaapcoomans.demo.microframeworks.ktor

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import io.ktor.routing.route
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository
import java.util.UUID

val todoRepository = InMemoryTodoRepository()
val todoService = TodoService(todoRepository)

fun Route.todoRoutes() {
    route("/todos") {
        get {
            call.respond(todoService.findAll().map { it.wrap() })
        }
        get("/{id}") {
            val id = call.parameters["id"]
            val todo = todoService.findById(UUID.fromString(id)).map { it.wrap() }
            if (todo.isPresent) {
                call.respond(todo.get())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            val command = call.receive<CreateTodoCommand>()
            val todo = todoService.createNewTodo(command.title, command.order)
            call.respond(todo.wrap())
        }
        delete {
            todoService.deleteAll()
            call.respond(HttpStatusCode.NoContent)
        }
        patch("/{id}") {
            val id = call.parameters["id"]
            val command = call.receive<PartialTodo>()

            val todo = todoService.updateTodo(UUID.fromString(id), command).map { it.wrap() }
            if (todo.isPresent) {
                call.respond(todo.get())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            todoService.delete(UUID.fromString(id))
            call.respond(HttpStatusCode.NoContent)
        }
    }
}

fun Todo.wrap(): TodoDTO {
    return TodoDTO.from(this, createUrl())
}

fun Todo.createUrl(): String {
    return "http://localhost:8080/todos/$id"
}