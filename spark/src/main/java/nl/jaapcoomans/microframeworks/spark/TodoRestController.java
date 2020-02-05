package nl.jaapcoomans.microframeworks.spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

class TodoRestController {
    private static final String ID_PATH_PARAM = "id";

    private TodoService todoService;
    private String baseUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    List<TodoDTO> getAll(Request request, Response response) {
        response.header("Content-Type", "application/json");

        return this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }


    Optional<TodoDTO> getTodo(Request request, Response response) {
        UUID id = UUID.fromString(request.params(ID_PATH_PARAM));

        response.header("Content-Type", "application/json");

        var result = this.todoService.findById(id)
                .map(this::wrap);

        if (result.isEmpty()) {
            response.status(404);
        }

        return result;
    }


    TodoDTO createTodo(Request request, Response response) throws Exception {
        var command = deserialize(request.body(), CreateTodoCommand.class);

        response.header("Content-Type", "application/json");

        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        return this.wrap(result);
    }

    String deleteAll(Request request, Response response) {
        this.todoService.deleteAll();
        response.status(HttpStatus.NO_CONTENT_204);
        return "";
    }


    Optional<TodoDTO> patchTodo(Request request, Response response) {
        var id = UUID.fromString(request.params(ID_PATH_PARAM));
        var command = deserialize(request.body(), PartialTodo.class);

        response.header("Content-Type", "application/json");

        return this.todoService.updateTodo(id, command)
                .map(this::wrap);
    }

    String deleteTodo(Request request, Response response) {
        UUID id = UUID.fromString(request.params(ID_PATH_PARAM));
        this.todoService.delete(id);

        response.status(204);
        return "";
    }

    private <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize to JSON");
        }
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
