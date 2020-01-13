package nl.jaapcoomans.microframeworks.helidon.mp;

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoRepository;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/todos")
public class TodoApplication extends Application {
    @Produces
    public TodoRepository todoRepository() {
        return new InMemoryTodoRepository();
    }

    @Produces
    public TodoService todoService(TodoRepository todoRepository) {
        return new TodoService(todoRepository);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(TodoController.class, CorsFilter.class);
    }
}
