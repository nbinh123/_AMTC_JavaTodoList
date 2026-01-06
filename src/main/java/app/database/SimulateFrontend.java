package app.database;

import java.time.LocalDate;
import java.util.List;

import app.model.TodoItem;
import app.service.TodoService;

public class SimulateFrontend {

    public static void main(String[] args) {

        TodoService backend = new TodoService();
        LocalDate today = LocalDate.now();

        System.out.println("=== GIẢ LẬP FRONTEND GỌI BACKEND ===\n");

        backend.addTask("Nộp bài tập Java", today);
        backend.addTask("Mua sữa", today);

        List<TodoItem> tasks = backend.getTasksByDate(today);

        System.out.println("Danh sách task:");
        tasks.forEach(t ->
            System.out.println("- " + t.getTitle() + " | done=" + t.isCompleted())
        );

        if (!tasks.isEmpty()) {
            backend.toggleCompleted(tasks.get(0));
        }

        long completed = tasks.stream().filter(TodoItem::isCompleted).count();

        System.out.println("\nTổng: " + tasks.size());
        System.out.println("Hoàn thành: " + completed);
    }
}
