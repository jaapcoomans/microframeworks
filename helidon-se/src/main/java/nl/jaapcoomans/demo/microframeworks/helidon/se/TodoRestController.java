package nl.jaapcoomans.demo.microframeworks.helidon.se;

import io.helidon.common.http.Http;
import io.helidon.webserver.Handler;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.helidon.common.http.Http.Method.DELETE;
import static io.helidon.common.http.Http.Method.GET;
import static io.helidon.common.http.Http.Method.POST;
import static io.helidon.common.http.Http.Status.*;

public class TodoRestController implements Service {
    private static final String ID_PATH_PARAM = "id";

    private static final Http.RequestMethod PATCH = Http.RequestMethod.create("PATCH");

    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    @Override
    public void update(Routing.Rules rules) {
        rules.anyOf(List.of(GET, DELETE, POST), this::corsFilter)
                .options(this::corsPreflight)
                .get("/", this::getAll)
                .post("/",
                        Handler.create(CreateTodoCommand.class, this::createTodo))
                .delete("/", this::deleteAll)
                .get("/{id}", this::getTodo)
                .delete("/{id}", this::deleteTodo)
                .anyOf(List.of(PATCH), "/{id}",
                        Handler.create(PartialTodo.class, this::patchTodo));
    }

    private void corsPreflight(ServerRequest request, ServerResponse response) {
        this.addCorsHeaders(response);
        response.status(OK_200).send();
    }

    private void corsFilter(ServerRequest request, ServerResponse response) {
        this.addCorsHeaders(response);
        request.next();
    }

    private void addCorsHeaders(ServerResponse response) {
        response.headers().add("Access-Control-Allow-Origin", "*");
        response.headers().add("Access-Control-Allow-Credentials", "true");
        response.headers().add("Access-Control-Allow-Headers", "content-type");
        response.headers().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

    private void getAll(ServerRequest request, ServerResponse response) {
        List<TodoDTO> todos = this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());

        response.send(todos);
    }

    private void getTodo(ServerRequest request, ServerResponse response) {
        UUID id = UUID.fromString(request.path().param(ID_PATH_PARAM));

        this.todoService.findById(id)
                .map(this::wrap)
                .ifPresentOrElse(response::send,
                        () -> response.status(NOT_FOUND_404).send());
    }

    private void createTodo(ServerRequest request, ServerResponse response, CreateTodoCommand command) {
        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        response.send(this.wrap(result));
    }

    private void deleteAll(ServerRequest request, ServerResponse response) {
        this.todoService.deleteAll();
        response.status(NO_CONTENT_204).send();
    }

    private void patchTodo(ServerRequest request, ServerResponse response, PartialTodo command) {
        UUID id = UUID.fromString(request.path().param(ID_PATH_PARAM));

        this.todoService.updateTodo(id, command)
                .map(this::wrap)
                .ifPresentOrElse(response::send, () -> response.status(NOT_FOUND_404).send());
    }

    private void deleteTodo(ServerRequest request, ServerResponse response) {
        UUID id = UUID.fromString(request.path().param(ID_PATH_PARAM));
        this.todoService.delete(id);
        response.status(NO_CONTENT_204).send();
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
