package nl.jaapcoomans.demo.microframeworks.todo.api;

public class CreateTodoCommand {
    private String title;
    private int order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
