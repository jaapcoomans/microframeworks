package nl.jaapcoomans.microframeworks.dropwizard;

import nl.jaapcoomans.demo.microframeworks.todo.api.CreateTodoCommand;
import nl.jaapcoomans.demo.microframeworks.todo.api.TodoDTO;
import nl.jaapcoomans.demo.microframeworks.todo.domain.PartialTodo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/todos")
public class TodoRestController {
    private TodoService todoService;
    private String baseUrl;

    TodoRestController(TodoService todoService, String baseUrl) {
        this.todoService = todoService;
        this.baseUrl = baseUrl;
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
    public Response getTodo(@PathParam("id") String id) {
        return this.todoService.findById(UUID.fromString(id))
                .map(this::wrap)
                .map(body -> Response.ok(body).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
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
