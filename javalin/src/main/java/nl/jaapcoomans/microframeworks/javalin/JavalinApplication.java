package nl.jaapcoomans.microframeworks.javalin;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.core.JavalinConfig;
import io.javalin.http.Context;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class JavalinApplication {
    private static final int PORT = 7000;

    public static void main(String[] args) {
        Javalin.create(JavalinApplication::configure)
                .routes(() -> {
                    path("/hello", helloWorldApi());
                    path("/todos", todoApi());
                })
                .start(PORT);
    }

    private static void configure(JavalinConfig config) {
        config.enableCorsForAllOrigins();
    }

    private static EndpointGroup helloWorldApi() {
        return () -> {
            get(JavalinApplication::helloWorld);
            get("/:name", JavalinApplication::hello);
        };
    }

    private static EndpointGroup todoApi() {
        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);
        var todoController = new TodoRestController(todoService, "http://localhost:7000/todos");

        return todoController.defineEndpoints();
    }

    private static void hello(Context context) {
        context.result(String.format("Hello, %s!", capitalize(context.pathParam("name"))));
    }

    private static void helloWorld(Context context) {
        context.result("Hello World!");
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
