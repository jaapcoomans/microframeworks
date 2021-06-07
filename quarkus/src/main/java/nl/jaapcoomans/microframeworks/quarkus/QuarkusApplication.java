package nl.jaapcoomans.microframeworks.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoRepository;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

import javax.enterprise.inject.Produces;

@QuarkusMain
public class QuarkusApplication {
    public static void main(String... args) {
        Quarkus.run(args);
    }

    @Produces
    public TodoService todoService(TodoRepository todoRepository) {
        return new TodoService(todoRepository);
    }

    @Produces
    public TodoRepository todoRepository() {
        return new InMemoryTodoRepository();
    }
}
