package nl.jaapcoomans.microframeworks.armeria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ConsumesJson;
import com.linecorp.armeria.server.annotation.Delete;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Patch;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesJson;
import com.linecorp.armeria.server.annotation.StatusCode;
import com.linecorp.armeria.server.annotation.decorator.CorsDecorator;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CorsDecorator(origins = "*", allowedRequestMethods = HttpMethod.GET, allowedRequestHeaders = "Content-Type")
class TodoRestController {
    private ObjectMapper objectMapper = new ObjectMapper();

    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    @Get("/todos")
    @ProducesJson
    public List<TodoDTO> getAll() {
        return this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    @Get("/todos/{id}")
    @ProducesJson
    public HttpResponse getTodo(@Param("id") String id) throws JsonProcessingException {
        return this.todoService.findById(UUID.fromString(id))
                .map(this::wrap)
                .map(todo -> HttpResponse.of(HttpStatus.OK, MediaType.JSON,
                        this.convertToJson(todo)))
                .orElse(HttpResponse.of(404));
    }

    private String convertToJson(TodoDTO todo) {
        try {
            return objectMapper.writeValueAsString(todo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Post("/todos")
    @ProducesJson
    @ConsumesJson
    public TodoDTO createTodo(CreateTodoCommand command) {
        Todo todo = this.todoService.createNewTodo(command.getTitle(),
                command.getOrder());
        return this.wrap(todo);
    }

    @Delete("/todos")
    @StatusCode(204)
    public void deleteAll() {
        this.todoService.deleteAll();
    }

    @Patch("/todos/{id}")
    @ConsumesJson
    @ProducesJson
    public TodoDTO patchTodo(@Param("id") String id, PartialTodo command) {
        return this.todoService.updateTodo(UUID.fromString(id), command)
                .map(this::wrap)
                .orElse(null);
    }

    @Delete("/todos/{id}")
    @StatusCode(204)
    public void deleteTodo(@Param("id") String id) {
        this.todoService.delete(UUID.fromString(id));
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
