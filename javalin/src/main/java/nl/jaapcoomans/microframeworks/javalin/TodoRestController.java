package nl.jaapcoomans.microframeworks.javalin;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.post;

class TodoRestController {
    private static final String ID_PATH_PARAM = "id";
    private static final String ID_PATH = "/:" + ID_PATH_PARAM;

    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    EndpointGroup defineEndpoints() {
        return () -> {
            get(this::getAll);
            post(this::createTodo);
            delete(this::deleteAll);
            get(ID_PATH, this::getTodo);
            patch(ID_PATH, this::patchTodo);
            delete(ID_PATH, this::deleteTodo);
        };
    }

    void getAll(Context context) {
        List<TodoDTO> todos = this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());

        context.json(todos);
    }

    void getTodo(Context context) {
        UUID id = UUID.fromString(context.pathParam(ID_PATH_PARAM));

        this.todoService.findById(id)
                .map(this::wrap)
                .ifPresentOrElse(context::json, () -> context.status(404));
    }

    void createTodo(Context context) {
        var command = context.bodyAsClass(CreateTodoCommand.class);

        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        context.json(this.wrap(result));
    }

    void deleteAll(Context context) {
        this.todoService.deleteAll();
        context.status(204);
    }

    void patchTodo(Context context) {
        UUID id = UUID.fromString(context.pathParam(ID_PATH_PARAM));

        var command = context.bodyAsClass(PartialTodo.class);

        this.todoService.updateTodo(id, command)
                .map(this::wrap)
                .ifPresentOrElse(context::json, () -> context.status(404));
    }

    void deleteTodo(Context context) {
        UUID id = UUID.fromString(context.pathParam(ID_PATH_PARAM));
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
