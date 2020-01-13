package nl.jaapcoomans.microframeworks.blade;

import com.blade.Blade;
import com.blade.mvc.RouteContext;
import com.blade.mvc.http.HttpMethod;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class TodoRestController {
    private static final String ID_PATH_PARAM = "id";

    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    void initializeRoutes(Blade application) {
        application.contextPath("/todos")
                .get("/", this::getAll)
                .post("/", this::createTodo)
                .delete("/", this::deleteAll)
                .get("/:id", this::getTodo)
                .delete("/:id", this::deleteTodo)
                .routeMatcher().addRoute("/:id", this::patchTodo, HttpMethod.PATCH);
    }

    private void getAll(RouteContext context) {
        List<TodoDTO> todos = this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());

        context.json(todos);
    }

    private void getTodo(RouteContext context) {
        UUID id = UUID.fromString(context.pathString(ID_PATH_PARAM));

        this.todoService.findById(id)
                .map(this::wrap)
                .ifPresentOrElse(context::json, () -> context.status(404));
    }

    private void createTodo(RouteContext context) {
        var command = context.request().bindWithBody(CreateTodoCommand.class);

        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        context.json(this.wrap(result));
    }

    private void deleteAll(RouteContext context) {
        this.todoService.deleteAll();
        context.status(204);
    }

    private void patchTodo(RouteContext context) {
        UUID id = UUID.fromString(context.pathString(ID_PATH_PARAM));

        var command = context.request().bindWithBody(PartialTodo.class);

        this.todoService.updateTodo(id, command)
                .map(this::wrap)
                .ifPresentOrElse(context::json, () -> context.status(404));
    }

    private void deleteTodo(RouteContext context) {
        UUID id = UUID.fromString(context.pathString(ID_PATH_PARAM));
        this.todoService.delete(id);
        context.status(204);
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
