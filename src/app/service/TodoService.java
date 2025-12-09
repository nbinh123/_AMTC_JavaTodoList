package app.service;

import app.model.TodoItem;
import app.model.TodoMongoRepository;

import java.util.List;

public class TodoService {

    private final TodoMongoRepository repository;

    public TodoService() {
        this.repository = new TodoMongoRepository();
        System.out.println("TodoService initialized with MongoDB");
    }

    // Lấy toàn bộ danh sách
    public List<TodoItem> getAll() {
        return repository.getAll();
    }

    // Thêm 1 task
    public void addTask(String title) {
        if (title == null || title.trim().isEmpty()) return;

        TodoItem newItem = new TodoItem(title.trim());
        repository.add(newItem);
        System.out.println("Task added to MongoDB: " + title);
    }

    // Xoá task
    public void removeTask(TodoItem item) {
        boolean removed = repository.remove(item);
        if (removed) {
            System.out.println("Task removed from MongoDB: " + item.getTitle());
        }
    }

    // Đánh dấu đã xong
    public void toggleCompleted(TodoItem item) {
        item.setCompleted(!item.isCompleted());
        // Cập nhật trong database
        repository.updateById(item.getId(), item);
        System.out.println("Task updated in MongoDB: " + item.getTitle() + 
                          " (completed: " + item.isCompleted() + ")");
    }

    // Update toàn bộ vào database (MongoDB auto-saves)
    public void save() {
        repository.save(); // Just logs message
    }
    
    // Thêm method mới: tìm task theo ID
    public TodoItem getTaskById(String id) {
        return repository.getById(id);
    }
    
    // Thêm method mới: đếm task
    public long countTasks() {
        return repository.count();
    }
    
    // Thêm method mới: tìm task đã hoàn thành/chưa hoàn thành
    public List<TodoItem> getTasksByCompletion(boolean completed) {
        return repository.findByCompleted(completed);
    }
}
