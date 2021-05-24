package nl.jaapcoomans.microframeworks.minijax;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/todos")
public class TodoController {
    private TodoService todoService;

    private String baseUrl = "http://localhost:8080/todos";

    @Inject
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @OPTIONS
    public Response options() {
        return corsResponse();
    }

    @OPTIONS
    @Path("{id:.*}")
    public Response options2() {
        return corsResponse();
    }

    private static Response corsResponse() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TodoDTO> getAll() {
        return this.todoService.findAll().stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TodoDTO getTodo(@PathParam("id") String id) {
        return this.todoService.findById(UUID.fromString(id))
                .map(this::wrap)
                .orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TodoDTO createTodo(CreateTodoCommand command) {
        Todo result = this.todoService.createNewTodo(command.getTitle(), command.getOrder());
        return this.wrap(result);
    }

    @DELETE
    public Response deleteAll() {
        this.todoService.deleteAll();
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TodoDTO patchTodo(@PathParam("id") String id, PartialTodo command) {
        return this.todoService.updateTodo(UUID.fromString(id), command)
                .map(this::wrap)
                .orElse(null);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") UUID id) {
        this.todoService.delete(id);
        return Response.noContent().build();
    }

    private TodoDTO wrap(Todo todo) {
        return TodoDTO.from(todo, createUrl(todo));
    }

    private String createUrl(Todo todo) {
        return this.baseUrl + "/" + todo.getId().toString();
    }
}
