package nl.jaapcoomans.demo.microframeworks.ratpack;

import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ratpack.handling.Handlers.byMethod;
import static ratpack.jackson.Jackson.fromJson;
import static ratpack.jackson.Jackson.json;

public class TodoRestController {
    private static final String ID_PATH_PARAM = "id";

    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    void initializeEndpoints(Chain chain) throws Exception {
        chain
                .path("todos", byMethod(chain.getRegistry(), spec ->
                                spec.get(this::getAll)
                                        .post(this::createTodo)
                                        .delete(this::deleteAll)
                        )
                )
                .path("todos/:id", byMethod(chain.getRegistry(), spec ->
                                spec.get(this::getTodo)
                                        .delete(this::deleteTodo)
                                        .patch(this::patchTodo)
                        )
                );
    }

    void getAll(Context context) {
        List<TodoDTO> todos = this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());

        context.render(json(todos));
    }

    void getTodo(Context context) {
        UUID id = UUID.fromString(context.getPathTokens().get(ID_PATH_PARAM));

        this.todoService.findById(id)
                .map(this::wrap)
                .map(Jackson::json)
                .ifPresentOrElse(context::render, () -> context.getResponse().status(Status.NOT_FOUND).send());
    }

    void createTodo(Context context) {
        context.parse(fromJson(CreateTodoCommand.class))
                .map(command -> this.todoService.createNewTodo(command.getTitle(), command.getOrder()))
                .map(this::wrap)
                .map(Jackson::json)
                .then(context::render);
    }

    void deleteAll(Context context) {
        this.todoService.deleteAll();
        context.getResponse().status(Status.NO_CONTENT).send();
    }

    void patchTodo(Context context) {
        UUID id = UUID.fromString(context.getPathTokens().get(ID_PATH_PARAM));

        context.parse(fromJson(PartialTodo.class))
                .map(command -> this.todoService.updateTodo(id, command))
                .then(todo -> todo.map(this::wrap)
                        .map(Jackson::json)
                        .ifPresentOrElse(context::render, () -> context.getResponse().status(Status.NOT_FOUND).send())
                );
    }

    void deleteTodo(Context context) {
        UUID id = UUID.fromString(context.getPathTokens().get(ID_PATH_PARAM));
        this.todoService.delete(id);
        context.getResponse().status(Status.NO_CONTENT).send();
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
