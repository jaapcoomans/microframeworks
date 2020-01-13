package nl.jaapcoomans.microframeworks.blade;

import com.blade.Blade;
import com.blade.kit.JsonKit;
import com.blade.mvc.RouteContext;
import com.blade.security.web.cors.CorsConfiger;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

import java.util.List;

public class BladeApplication {
    public static void main(String[] args) {
        /*
         * The default JsonKit is not configurable and fails to correctly serialize UUID. Luckily the actual
         * serialization is pluggable. Therefore created a custom implementation based on Jackson.
         */
        JsonKit.jsonSupprt(new JacksonJsonSupport());

        Blade application = Blade.of()
                .enableCors(true, corsConfiger())
                .get("/hello", BladeApplication::helloWorld)
                .get("/hello/:name", BladeApplication::hello);

        createTodoBackend(application);

        application.start(BladeApplication.class, args);
    }

    private static CorsConfiger corsConfiger() {
        return CorsConfiger.builder()
                .allowedHeaders(List.of("Content-Type"))
                .build();
    }

    private static void createTodoBackend(Blade application) {
        var todoRepository = new InMemoryTodoRepository();
        var todoService = new TodoService(todoRepository);
        var todoApi = new TodoRestController(todoService, "http://localhost:7000/todos");

        todoApi.initializeRoutes(application);
    }

    private static void helloWorld(RouteContext context) {
        context.text("Hello World!");
    }

    private static void hello(RouteContext context) {
        String name = context.pathString("name");
        context.text(String.format("Hello, %s!", capitalize(name)));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
