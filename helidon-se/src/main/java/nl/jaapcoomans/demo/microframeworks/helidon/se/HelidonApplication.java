package nl.jaapcoomans.demo.microframeworks.helidon.se;

import io.helidon.media.jackson.JacksonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.cors.CorsSupport;
import io.helidon.webserver.cors.CrossOriginConfig;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

public class HelidonApplication {
    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();
        var server = WebServer.builder(HelidonApplication::createRouting)
                .addMediaSupport(JacksonSupport.create())
                .port(8080).build();

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
                .register("/hello", helloWorldRestController)
                .register("/todos", createCorsSupport(), todoRestController)
                .build();
    }

    private static CorsSupport createCorsSupport() {
        return CorsSupport.builder()
                .addCrossOrigin(CrossOriginConfig.builder()
                        .allowOrigins("*")
                        .allowMethods("GET", "POST", "PUT", "DELETE")
                        .build())
                .addCrossOrigin(CrossOriginConfig.create())
                .build();
    }

    private static TodoRestController createTodoBackend() {
        var repository = new InMemoryTodoRepository();
        var todoService = new TodoService(repository);

        return new TodoRestController(todoService, "http://localhost:8080/todos");
    }
}
