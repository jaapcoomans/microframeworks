package nl.jaapcoomans.demo.microframeworks.micronaut;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpResponseFactory;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/todos")
public class TodoController {
    private TodoService todoService;
    private String baseUrl;

    @Inject
    public TodoController(TodoService todoService, @Value("${todos.baseUrl}") String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<TodoDTO> getAll() {
        return this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse getTodo(@PathVariable String id) {
        return this.todoService.findById(UUID.fromString(id))
                .map(this::wrap)
                .map(HttpResponseFactory.INSTANCE::ok)
                .orElse(HttpResponseFactory.INSTANCE.status(HttpStatus.NOT_FOUND));
    }

    @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public TodoDTO createTodo(CreateTodoCommand command) {
        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        return this.wrap(result);
    }

    @Delete
    public HttpResponse deleteAll() {
        this.todoService.deleteAll();
        return HttpResponseFactory.INSTANCE.status(HttpStatus.NO_CONTENT);
    }

    @Patch(uri = "/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public TodoDTO patchTodo(@PathVariable String id, PartialTodo command) {
        return this.todoService.updateTodo(UUID.fromString(id), command)
                .map(this::wrap)
                .orElse(null);
    }

    @Delete(uri = "/{id}")
    public HttpResponse deleteTodo(@PathVariable UUID id) {
        this.todoService.delete(id);
        return HttpResponseFactory.INSTANCE.status(HttpStatus.NO_CONTENT);
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
