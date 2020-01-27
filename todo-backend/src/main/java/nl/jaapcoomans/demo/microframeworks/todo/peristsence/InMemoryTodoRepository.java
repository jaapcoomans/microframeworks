package nl.jaapcoomans.demo.microframeworks.todo.peristsence;

import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryTodoRepository implements TodoRepository {
    private static final Map<UUID, Todo> TODOS = new HashMap<>();

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(TODOS.values());
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return Optional.ofNullable(TODOS.get(id));
    }

    @Override
    public void delete(UUID id) {
        TODOS.remove(id);
    }

    @Override
    public Todo persist(Todo todo) {
        TODOS.put(todo.getId(), todo);
        return todo;
    }

    @Override
    public void deleteAll() {
        TODOS.clear();
    }
}
