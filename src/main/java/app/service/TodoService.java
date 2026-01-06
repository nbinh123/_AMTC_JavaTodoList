package app.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import app.model.TodoItem;
import app.model.TodoMongoRepository;
import app.model.User;
import app.model.UserMongoRepository;
import app.session.UserSession;
import app.utils.PasswordUtils;

public class TodoService {

    private final TodoMongoRepository todoRepository;
    private final UserMongoRepository userRepository;
    private final UserSession session = UserSession.getInstance();

    public TodoService() {
        this.todoRepository = new TodoMongoRepository();
        this.userRepository = new UserMongoRepository();
        System.out.println("TodoService initialized with MongoDB");
    }

    // ==================== AUTH ====================

    /**
     * Chỉ xác thực – KHÔNG login
     */
    public User authenticate(String username, String password) {
        return userRepository.authenticate(username, password);
    }

    private boolean isLoggedIn() {
        return session.isLoggedIn();
    }

    private User getCurrentUser() {
        return session.getUser();
    }

    private String getCurrentUserId() {
        return session.getUser().getUsername(); // hoặc getId()
    }

    // ==================== USER MANAGEMENT ====================

    public boolean registerUser(String username, String email, String password) {

        if (username == null || username.isBlank()
            || email == null || email.isBlank()
            || password == null || password.isBlank()) {
            System.err.println("Register failed: Invalid input");
            return false;
        }

        if (userRepository.existsByUsername(username)) {
            System.err.println("Register failed: Username already exists");
            return false;
        }

        if (userRepository.existsByEmail(email)) {
            System.err.println("Register failed: Email already exists");
            return false;
        }

        String hashedPassword = PasswordUtils.hash(password);

        User newUser = new User(
            username.trim(),
            hashedPassword,
            email.trim()
        );

        boolean success = userRepository.register(newUser);

        if (success) {
            System.out.println("User registered: " + username);
        }

        return success;
    }

    // ==================== TODO MANAGEMENT ====================

    public List<TodoItem> getAll() {
        if (!isLoggedIn()) {
            System.err.println("No user logged in!");
            return List.of();
        }
        return todoRepository.loadByUserId(getCurrentUserId());
    }

    public void addTask(String title) {
        if (!isLoggedIn()) {
            System.err.println("Cannot add task: No user logged in!");
            return;
        }

        if (title == null || title.trim().isEmpty()) return;

        TodoItem newItem = new TodoItem(title.trim());
        todoRepository.add(newItem, getCurrentUserId());

        System.out.println(
            "Task added for user " + getCurrentUser().getUsername() + ": " + title
        );
    }

    public void addTask(String title, LocalDate date) {
        if (!isLoggedIn()) {
            System.err.println("Cannot add task: No user logged in!");
            return;
        }

        TodoItem item = new TodoItem(title, date);
        todoRepository.add(item, getCurrentUserId());
    }

    public void removeTask(TodoItem item) {
        if (!isLoggedIn()) {
            System.err.println("Cannot remove task: No user logged in!");
            return;
        }

        boolean removed = todoRepository.remove(item.getId(), getCurrentUserId());
        if (!removed) {
            System.err.println("Cannot remove task: Not found or not owned by user");
        }
    }

    public void toggleCompleted(TodoItem item) {
        if (!isLoggedIn()) {
            System.err.println("Cannot toggle task: No user logged in!");
            return;
        }

        item.setCompleted(!item.isCompleted());
        todoRepository.updateById(item.getId(), item, getCurrentUserId());
    }

    public long countTasks() {
        if (!isLoggedIn()) return 0;
        return todoRepository.countByUserId(getCurrentUserId());
    }

    public List<TodoItem> getTasksByMonth(YearMonth yearMonth) {
        return getAll().stream()
            .filter(item -> {
                LocalDate d = item.getDate();
                return d.getYear() == yearMonth.getYear()
                    && d.getMonthValue() == yearMonth.getMonthValue();
            })
            .collect(Collectors.toList());
    }

    public List<TodoItem> getTasksByDate(LocalDate date) {
        return getAll().stream()
            .filter(t -> t.getDate() != null && t.getDate().equals(date))
            .collect(Collectors.toList());
    }

}
