package nl.jaapcoomans.demo.microframeworks.pippo;

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;
import ro.pippo.core.HttpConstants;
import ro.pippo.core.Pippo;
import ro.pippo.core.route.CorsHandler;
import ro.pippo.core.route.RouteContext;

public class PippoApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Pippo pippo = new Pippo();
        pippo.getServer().getSettings().host("0.0.0.0");
        pippo.getServer().setPort(PORT);

        pippo.ANY("/.*", createCorsHandler());
        pippo.GET("/hello", PippoApplication::helloWorld);
        pippo.GET("/hello/{name}", PippoApplication::hello);

        var todoRestController = createTodoBackend();
        todoRestController.initializeRoutes(pippo);

        pippo.start();
    }

    private static CorsHandler createCorsHandler() {
        var corsHandler = new CorsHandler("*");
        corsHandler.allowHeaders(HttpConstants.Header.CONTENT_TYPE);
        corsHandler.allowMethods("GET,POST,PATCH,DELETE");

        return corsHandler;
    }

    private static void initializeCors(Pippo pippo) {
        var corsHandler = new CorsHandler("*");
        corsHandler.allowHeaders(HttpConstants.Header.CONTENT_TYPE);
        corsHandler.allowMethods("GET,POST,PATCH,DELETE");

        pippo.ANY("/.*", corsHandler);
    }

    private static TodoRestController createTodoBackend() {
        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);

        return new TodoRestController(todoService, "http://localhost:8080/todos");
    }

    private static void helloWorld(RouteContext context) {
        context.text().send("Hello World!");
    }

    private static void hello(RouteContext context) {
        var name = context.getParameter("name").toString();
        context.text().send(String.format("Hello, %s!", capitalize(name)));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
