package app.model;

public class TodoItem {
    private String id;
    private String title;
    private boolean completed;
    
    public TodoItem() {
        this.id = java.util.UUID.randomUUID().toString();
    }
    
    public TodoItem(String title) {
        this();
        this.title = title;
    }
    
    public TodoItem(String title, boolean completed) {
        this();
        this.title = title;
        this.completed = completed;
    }
    
    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    @Override
    public String toString() {
        return title + (completed ? " (done)" : "");
    }
}
