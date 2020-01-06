package nl.jaapcoomans.demo.microframeworks.todo.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TodoService {
    private TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<Todo> findAll() {
        return this.repository.findAll();
    }

    public Optional<Todo> findById(UUID id) {
        return repository.findById(id);
    }

    public Todo createNewTodo(String title, int order) {
        Todo todo = Todo.create(title, order);

        return this.repository.persist(todo);
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }

    public void delete(UUID id) {
        this.repository.delete(id);
    }

    public Optional<Todo> updateTodo(UUID id, PartialTodo command) {
        return this.repository.findById(id)
                .map(todo -> todo.update(command))
                .map(repository::persist);
    }
}
