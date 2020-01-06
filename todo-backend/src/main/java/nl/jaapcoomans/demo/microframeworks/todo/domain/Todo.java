package nl.jaapcoomans.demo.microframeworks.todo.domain;

import java.util.UUID;

public class Todo {
    private UUID id;
    private String title;
    private boolean completed;
    private int order;

    private Todo(UUID id, String title, boolean completed, int order) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
    }

    static Todo create(String title, int order) {
        return new Todo(UUID.randomUUID(), title, false, order);
    }

    Todo changeTitle(String title) {
        return new Todo(this.id, title, this.completed, this.order);
    }

    Todo complete() {
        return new Todo(this.id, this.title, true, this.order);
    }

    Todo restore() {
        return new Todo(this.id, this.title, false, this.order);
    }

    Todo reorder(int order) {
        return new Todo(this.id, this.title, this.completed, order);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getOrder() {
        return order;
    }

    Todo update(PartialTodo todo) {
        return new Todo(
                this.id,
                todo.title().orElse(this.title),
                todo.completed().orElse(this.completed),
                todo.order().orElse(this.order)
        );
    }
}
