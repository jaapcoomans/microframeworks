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

    public Optional<Todo> changeTitle(UUID id, String title) {
        return this.repository.findById(id)
                .map(todo -> todo.changeTitle(title))
                .map(repository::persist);
    }

    public Optional<Todo> complete(UUID id) {
        return this.repository.findById(id)
                .map(Todo::complete)
                .map(repository::persist);
    }

    public Optional<Todo> restore(UUID id) {
        return this.repository.findById(id)
                .map(Todo::restore)
                .map(repository::persist);
    }

    public Optional<Todo> reorder(UUID id, int order) {
        return this.repository.findById(id)
                .map(todo -> todo.reorder(order))
                .map(repository::persist);
    }

    public void delete(UUID id) {
        this.repository.delete(id);
    }
}
