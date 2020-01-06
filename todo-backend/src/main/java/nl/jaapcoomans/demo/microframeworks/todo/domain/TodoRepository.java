package nl.jaapcoomans.demo.microframeworks.todo.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository {
    Todo persist(Todo todo);

    List<Todo> findAll();

    void deleteAll();

    Optional<Todo> findById(UUID id);

    void delete(UUID id);
}
