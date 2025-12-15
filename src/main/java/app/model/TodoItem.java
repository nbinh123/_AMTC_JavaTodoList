package app.model;

import java.time.LocalDate;

public class TodoItem {
    private String id;
    private String title;
    private boolean completed;
    private LocalDate date; // ✅ Đã có field này
    
    public TodoItem() {
        this.id = java.util.UUID.randomUUID().toString();
        this.date = LocalDate.now(); // ✅ Mặc định là hôm nay
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
    
    // ✅ THÊM constructor mới với date
    public TodoItem(String title, LocalDate date) {
        this();
        this.title = title;
        this.date = date;
    }
    
    // ✅ THÊM constructor đầy đủ
    public TodoItem(String title, LocalDate date, boolean completed) {
        this();
        this.title = title;
        this.date = date;
        this.completed = completed;
    }
    
    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    // ✅ THÊM getter và setter cho date
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    @Override
    public String toString() {
        return title + (completed ? " (done)" : "") + " [" + date + "]";
    }
}