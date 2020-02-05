package nl.jaapcoomans.microframeworks.armeria;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.ServiceRequestContext;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ArmeriaApplication {
    private static final int PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(ArmeriaApplication.class);

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        ServerBuilder sb = Server.builder();
        sb.http(PORT);

        sb.service("/hello", ArmeriaApplication::helloWorld);
        sb.service("/hello/{name}", ArmeriaApplication::hello);

        TodoRestController todoBackendApi = createTodoBackend();
        sb.annotatedService(todoBackendApi);

        Server server = sb.build();
        CompletableFuture<Void> future = server.start();
        future.join();

        LOGGER.info("Started in {}   ms", System.currentTimeMillis() - startTime);
    }

    private static TodoRestController createTodoBackend() {
        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);
        return new TodoRestController(todoService, "http://localhost:8080/todos");
    }

    private static HttpResponse helloWorld(ServiceRequestContext context, HttpRequest request) {
        return HttpResponse.of("Hello World!");
    }

    private static HttpResponse hello(ServiceRequestContext context, HttpRequest request) {
        String name = context.pathParam("name");
        return HttpResponse.of("Hello, %s!", capitalize(name));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
