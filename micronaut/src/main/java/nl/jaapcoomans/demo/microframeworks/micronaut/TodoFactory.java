package nl.jaapcoomans.demo.microframeworks.micronaut;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

@Factory
public class TodoFactory {
    @Bean()
    public TodoService todoService() {
        return new TodoService(new InMemoryTodoRepository());
    }
}
