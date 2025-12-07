package app.model;

public class TodoItem {
    private String title;
    private boolean completed;

    public TodoItem(String title) {
        this(title, false);
    }

    public TodoItem(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }

    public void setTitle(String title) { this.title = title; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return title + (completed ? " (done)" : "");
    }
}
