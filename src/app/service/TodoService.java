package app.service;

import app.model.TodoItem;
import app.model.TodoRepository;

import java.util.List;

public class TodoService {

    private final TodoRepository repository;

    public TodoService() {
        this.repository = new TodoRepository();
    }

    // Lấy toàn bộ danh sách
    public List<TodoItem> getAll() {
        return repository.getAll();
    }

    // Thêm 1 task
    public void addTask(String title) {
        if (title == null || title.trim().isEmpty()) return;

        repository.add(new TodoItem(title.trim()));
        repository.save();
    }

    // Xoá task
    public void removeTask(TodoItem item) {
        repository.remove(item);
        repository.save();
    }

    // Đánh dấu đã xong
    public void toggleCompleted(TodoItem item) {
        item.setCompleted(!item.isCompleted());
        repository.save();
    }

    // Update toàn bộ vào file
    public void save() {
        repository.save();
    }
}
