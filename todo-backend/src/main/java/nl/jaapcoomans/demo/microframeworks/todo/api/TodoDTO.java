package nl.jaapcoomans.demo.microframeworks.todo.api;

import nl.jaapcoomans.demo.microframeworks.todo.domain.Todo;

import java.util.UUID;

public class TodoDTO {
    private UUID id;
    private String title;
    private String url;
    private boolean completed;
    private int order;

    private TodoDTO(UUID id, String title, String url, boolean completed, int order) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.completed = completed;
        this.order = order;
    }

    public static TodoDTO from(Todo todo, String url) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                url,
                todo.isCompleted(),
                todo.getOrder()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getOrder() {
        return order;
    }
}
