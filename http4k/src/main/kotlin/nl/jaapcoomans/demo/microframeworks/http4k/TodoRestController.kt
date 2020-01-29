package nl.jaapcoomans.demo.microframeworks.http4k

import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService
import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson
import org.http4k.format.Jackson.asJsonObject
import org.http4k.routing.path
import java.util.UUID

class TodoRestController(val todoService: TodoService) {
    private val json = Jackson

    fun getAll(req: Request): Response {
        val todos = todoService.findAll().map { it.wrap() }
        return Response(Status.OK)
                .header("Content-Type", ContentType.APPLICATION_JSON.value)
                .body(todos.asJsonObject().toPrettyString())
    }

    fun createTodo(req: Request): Response {
        val command = json.asA(req.bodyString(), CreateTodoCommand::class)

        val todo = todoService.createNewTodo(command.title, command.order)

        return Response(Status.OK)
                .header("Content-Type", ContentType.APPLICATION_JSON.value)
                .body(todo.wrap().asJsonObject().toPrettyString())
    }

    fun getTodo(req: Request): Response {
        val id = req.path("id")
        val todo = todoService.findById(UUID.fromString(id))
        return if (todo.isPresent) {
            Response(Status.OK)
                    .header("Content-Type", ContentType.APPLICATION_JSON.value)
                    .body(todo.get().wrap().asJsonObject().toPrettyString())
        } else {
            Response(Status.NOT_FOUND)
        }
    }

    fun patchTodo(req: Request): Response {
        val id = req.path("id")
        val command = json.asA(req.bodyString(), PartialTodo::class)

        val todo = todoService.updateTodo(UUID.fromString(id), command)
        return if (todo.isPresent) {
            Response(Status.OK)
                    .header("Content-Type", ContentType.APPLICATION_JSON.value)
                    .body(todo.get().wrap().asJsonObject().toPrettyString())
        } else {
            Response(Status.NOT_FOUND)
        }
    }

    fun deleteTodo(req: Request): Response {
        val id = req.path("id")
        todoService.delete(UUID.fromString(id))
        return Response(Status.NO_CONTENT)
    }

    fun deleteAll(req: Request): Response {
        todoService.deleteAll()
        return Response(Status.NO_CONTENT)
    }

    private fun Todo.wrap(): TodoDTO {
        return TodoDTO.from(this, createUrl())
    }

    private fun Todo.createUrl(): String {
        return "http://localhost:8080/todos/$id"
    }
}