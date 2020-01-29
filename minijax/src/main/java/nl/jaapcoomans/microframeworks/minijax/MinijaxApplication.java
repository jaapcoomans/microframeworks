package nl.jaapcoomans.microframeworks.minijax;

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;
import org.minijax.Minijax;
import org.minijax.json.JsonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinijaxApplication.class);

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        var todoRepository = new InMemoryTodoRepository();

        new Minijax()
                .register(JsonFeature.class)
                .register(CorsFilter.class)
                .register(HelloWorldController.class)
                .register(new TodoService(todoRepository))
                .register(TodoController.class)
                .start();

        LOGGER.info("Started in {} ms", System.currentTimeMillis() - startTime);
    }
}
