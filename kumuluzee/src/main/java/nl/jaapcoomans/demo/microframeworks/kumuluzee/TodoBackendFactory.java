package nl.jaapcoomans.demo.microframeworks.kumuluzee;

import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoRepository;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TodoBackendFactory {
    @Produces
    public TodoRepository todoRepository() {
        return new InMemoryTodoRepository();
    }

    @Produces
    public TodoService todoService() {
        return new TodoService(todoRepository());
    }

}
