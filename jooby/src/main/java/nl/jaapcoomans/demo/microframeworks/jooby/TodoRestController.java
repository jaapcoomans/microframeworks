package nl.jaapcoomans.demo.microframeworks.jooby;

import io.jooby.Context;
import io.jooby.Jooby;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import javax.annotation.Nonnull;
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

    void initializeRoutes(Jooby app) {
        app.get("/todos", this::getAll);
        app.post("/todos", this::createTodo);
        app.delete("/todos", this::deleteAll);
        app.get("/todos/{id}", this::getTodo);
        app.patch("/todos/{id}", this::patchTodo);
        app.delete("/todos/{id}", this::deleteTodo);
    }

    @Nonnull
    private List<TodoDTO> getAll(Context context) {
        return this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    @Nonnull
    private TodoDTO getTodo(Context context) {
        UUID id = UUID.fromString(context.path(ID_PATH_PARAM).value());

        return this.todoService.findById(id)
                .map(this::wrap)
                .orElseThrow(() -> new StatusCodeException(StatusCode.NOT_FOUND));
    }

    private TodoDTO createTodo(Context context) {
        var command = context.body(CreateTodoCommand.class);

        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        return this.wrap(result);
    }

    private Context deleteAll(Context context) {
        this.todoService.deleteAll();
        context.send(StatusCode.NO_CONTENT);
        return context;
    }

    @Nonnull
    private TodoDTO patchTodo(Context context) {
        UUID id = UUID.fromString(context.path(ID_PATH_PARAM).value());

        var command = context.body(PartialTodo.class);

        return this.todoService.updateTodo(id, command)
                .map(this::wrap)
                .orElseThrow(() -> new StatusCodeException(StatusCode.NOT_FOUND));
    }

    private Context deleteTodo(Context context) {
        UUID id = UUID.fromString(context.path(ID_PATH_PARAM).value());
        this.todoService.delete(id);
        context.send(StatusCode.NO_CONTENT);
        return context;
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }

}
