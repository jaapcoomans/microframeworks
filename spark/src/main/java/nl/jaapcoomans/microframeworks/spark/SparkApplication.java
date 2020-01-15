package nl.jaapcoomans.microframeworks.spark;

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;
import spark.Request;
import spark.Response;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.patch;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;

public class SparkApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        port(PORT);
        addCORS();

        get("/hello", SparkApplication::helloWorld);
        get("/hello/:name", SparkApplication::hello);

        setupTodoBackend();
    }

    private static void setupTodoBackend() {
        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);
        var todoRestController = new TodoRestController(todoService, "http://localhost:8080/todos");

        var jsonTransformer = new JacksonResponseTransformer();

        path("/todos", () -> {
            get("", todoRestController::getAll, jsonTransformer);
            post("", todoRestController::createTodo, jsonTransformer);
            delete("", todoRestController::deleteAll);
            path("/:id", () -> {
                get("", todoRestController::getTodo, jsonTransformer);
                delete("", todoRestController::deleteTodo);
                patch("", todoRestController::patchTodo, jsonTransformer);
            });
        });
    }

    private static String helloWorld(Request request, Response response) {
        return "Hello World!";
    }

    private static String hello(Request request, Response response) {
        return String.format("Hello, %s!", capitalize(request.params("name")));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }

    private static void addCORS() {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    }
}
