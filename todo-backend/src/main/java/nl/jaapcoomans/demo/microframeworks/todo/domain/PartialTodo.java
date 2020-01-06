package nl.jaapcoomans.demo.microframeworks.todo.domain;

import java.util.Optional;

public class PartialTodo {
    private String title;
    private Boolean completed;
    private Integer order;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    Optional<String> title() {
        return Optional.ofNullable(this.title);
    }

    Optional<Boolean> completed() {
        return Optional.ofNullable(this.completed);
    }

    Optional<Integer> order() {
        return Optional.ofNullable(this.order);
    }
}
