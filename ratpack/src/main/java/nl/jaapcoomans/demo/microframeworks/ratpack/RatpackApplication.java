package nl.jaapcoomans.demo.microframeworks.ratpack;

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.http.HttpMethod;
import ratpack.http.Status;
import ratpack.server.RatpackServer;
import ratpack.server.RatpackServerSpec;
import ratpack.server.ServerConfig;

public class RatpackApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatpackApplication.class);
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        RatpackServer.start(RatpackApplication::init);

        LOGGER.info("Started in {} ms", System.currentTimeMillis() - startTime);
    }

    private static void init(RatpackServerSpec server) {
        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);
        var todoRestController = new TodoRestController(todoService, "http://localhost:8080/todos");

        server.serverConfig(ServerConfig.embedded().port(PORT))
                .handlers(chain -> {
                    chain.all(RatpackApplication::cors)
                            .get("hello", RatpackApplication::helloWorld)
                            .get("hello/:name", RatpackApplication::hello);
                    todoRestController.initializeEndpoints(chain);
                });
    }

    private static void cors(Context context) {
        context.header("Access-Control-Allow-Origin", "*");
        if (context.getRequest().getMethod() == HttpMethod.OPTIONS) {
            context.header("Access-Control-Allow-Methods", "GET,POST,PATCH,DELETE");
            context.header("Access-Control-Allow-Headers", "content-type");
            context.getResponse().status(Status.OK).send();
        } else {
            context.next();
        }
    }

    private static void helloWorld(Context context) {
        context.getResponse()
                .contentType("text/plain")
                .send("Hello World");
    }

    private static void hello(Context context) {
        var name = context.getPathTokens().get("name");
        context.getResponse()
                .contentType("text/plain")
                .send(String.format("Hello, %s!", capitalize(name)));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
