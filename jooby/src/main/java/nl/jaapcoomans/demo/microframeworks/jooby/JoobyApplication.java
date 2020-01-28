package nl.jaapcoomans.demo.microframeworks.jooby;

import io.jooby.Context;
import io.jooby.CorsHandler;
import io.jooby.MediaType;
import io.jooby.ServerOptions;
import io.jooby.json.JacksonModule;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

import javax.annotation.Nonnull;

import static io.jooby.Jooby.runApp;

public class JoobyApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        var jacksonModule = new JacksonModule();

        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);
        var todoController = new TodoRestController(todoService, "http://localhost:8080/todos");

        runApp(args, app -> {
            app.setServerOptions(new ServerOptions().setPort(PORT));
            app.encoder(MediaType.json, jacksonModule);
            app.decoder(MediaType.json, jacksonModule);
            app.decorator(new CorsHandler());

            app.get("/hello", JoobyApplication::helloWorld);
            app.get("/hello/{name}", JoobyApplication::hello);

            todoController.initializeRoutes(app);
        });
    }

    private static String helloWorld(Context context) {
        return "Hello World!";
    }

    @Nonnull
    private static String hello(Context context) {
        var name = context.path("name").value();
        return String.format("Hello, %s!", capitalize(name));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
