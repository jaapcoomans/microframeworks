package nl.jaapcoomans.demo.microframeworks.todo.api;

public class UpdateTodoCommand {
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

    public boolean hasTitle() {
        return this.title != null;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleteCommand() {
        return this.completed == Boolean.TRUE;
    }

    public boolean isRestoreCommand() {
        return this.completed == Boolean.FALSE;
    }

    public boolean hasOrder() {
        return this.order != null;
    }

    public int getOrder() {
        return this.order;
    }
}
