package nl.jaapcoomans.demo.microframeworks.pippo;

import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import ro.pippo.core.Pippo;
import ro.pippo.core.route.RouteContext;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoRestController {
    private static final String ID_PATH_PARAM = "id";

    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    void initializeRoutes(Pippo pippo) {
        pippo.GET("/todos", this::getAll);
        pippo.POST("/todos", this::createTodo);
        pippo.DELETE("/todos", this::deleteAll);
        pippo.GET("/todos/{id}", this::getTodo);
        pippo.PATCH("/todos/{id}", this::patchTodo);
        pippo.DELETE("/todos/{id}", this::deleteTodo);
    }

    private void getAll(RouteContext context) {
        List<TodoDTO> todos = this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());

        context.json().send(todos);
    }

    private void getTodo(RouteContext context) {
        UUID id = UUID.fromString(context.getParameter(ID_PATH_PARAM).toString());

        this.todoService.findById(id)
                .map(this::wrap)
                .ifPresentOrElse(context.json()::send, () -> context.status(404));
    }

    private void createTodo(RouteContext context) {
        var command = context.createEntityFromBody(CreateTodoCommand.class);

        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        context.json().send(this.wrap(result));
    }

    private void deleteAll(RouteContext context) {
        this.todoService.deleteAll();
        context.status(204);
    }

    private void patchTodo(RouteContext context) {
        UUID id = UUID.fromString(context.getParameter(ID_PATH_PARAM).toString());

        var command = context.createEntityFromBody(PartialTodo.class);

        this.todoService.updateTodo(id, command)
                .map(this::wrap)
                .ifPresentOrElse(context.json()::send, () -> context.status(404));
    }

    private void deleteTodo(RouteContext context) {
        UUID id = UUID.fromString(context.getParameter(ID_PATH_PARAM).toString());
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
