package nl.jaapcoomans.demo.microframeworks.helidon.se;

import io.helidon.media.jsonb.server.JsonBindingSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

public class HelidonApplication {
    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();
        ServerConfiguration serverConfig =
                ServerConfiguration.builder().port(8080).build();

        WebServer server = WebServer.create(serverConfig, createRouting());

        // Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
        server.start()
                .thenAccept(ws -> {
                    long bootTime = System.currentTimeMillis() - startTime;
                    System.out.println(
                            "WEB server is up! http://localhost:" + ws.port() + "/ in " + bootTime + "ms");
                    ws.whenShutdown().thenRun(()
                            -> System.out.println("WEB server is DOWN. Good bye!"));
                })
                .exceptionally(t -> {
                    System.err.println("Startup failed: " + t.getMessage());
                    t.printStackTrace(System.err);
                    return null;
                });
    }

    private static Routing createRouting() {
        HelloWorldRestController helloWorldRestController = new HelloWorldRestController();

        TodoRestController todoRestController = createTodoBackend();

        return Routing.builder()
                .register(JsonBindingSupport.create())
                .register("/hello", helloWorldRestController)
                .register("/todos", todoRestController)
                .build();
    }

    private static TodoRestController createTodoBackend() {
        var repository = new InMemoryTodoRepository();
        var todoService = new TodoService(repository);

        return new TodoRestController(todoService, "http://localhost:8080/todos");
    }
}
