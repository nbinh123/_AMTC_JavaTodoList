package app.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple repository lưu tasks vào file text (tasks.txt).
 * Format mỗi dòng: title \t completed (true/false)
 */
public class TodoRepository {

    private final Path filePath;
    private final List<TodoItem> items = new ArrayList<>();

    public TodoRepository() {
        this("tasks.txt");
    }

    public TodoRepository(String filename) {
        this.filePath = Paths.get(filename);
        // load khi khởi tạo
        load();
    }

    public List<TodoItem> load() {
        items.clear();
        if (!Files.exists(filePath)) {
            // tạo file rỗng nếu chưa có
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                // nếu không thể tạo, vẫn tiếp tục với list rỗng
                System.err.println("Cannot create data file: " + e.getMessage());
            }
            return items;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\t", 2);
                String title = parts.length > 0 ? parts[0] : "";
                boolean completed = false;
                if (parts.length > 1) {
                    completed = Boolean.parseBoolean(parts[1]);
                }
                items.add(new TodoItem(title, completed));
            }
        } catch (IOException e) {
            System.err.println("Error reading tasks file: " + e.getMessage());
        }

        return new ArrayList<>(items); // trả về sao chép để tránh thay đổi trực tiếp
    }

    public void add(TodoItem item) {
        items.add(item);
    }

    public void remove(TodoItem item) {
        items.remove(item);
    }

    public void update(int index, TodoItem newItem) {
        if (index >= 0 && index < items.size()) {
            items.set(index, newItem);
        }
    }

    public List<TodoItem> getAll() {
        return new ArrayList<>(items);
    }

    public void save() {
        List<String> lines = new ArrayList<>();
        for (TodoItem it : items) {
            String titleEscaped = it.getTitle().replace("\t", " "); // tránh tab trong title
            lines.add(titleEscaped + "\t" + Boolean.toString(it.isCompleted()));
        }
        try {
            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving tasks file: " + e.getMessage());
        }
    }
}
