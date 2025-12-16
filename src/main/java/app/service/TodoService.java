package app.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import app.model.TodoItem;
import app.model.TodoMongoRepository;
import app.model.User;
import app.model.UserMongoRepository;
import app.ulti.PasswordUtils;

public class TodoService {

    private final TodoMongoRepository todoRepository;
    private final UserMongoRepository userRepository;
    
    // Lưu user hiện tại đang đăng nhập
    private User currentUser;

    public TodoService() {
        this.todoRepository = new TodoMongoRepository();
        this.userRepository = new UserMongoRepository();
        System.out.println("TodoService initialized with MongoDB");
    }

    // ==================== USER MANAGEMENT ====================
    
    /**
     * Đăng ký user mới
     */
    public boolean registerUser(String username, String email, String password) {

    // 1. Validate input
        if (username == null || username.isBlank()
            || email == null || email.isBlank()
            || password == null || password.isBlank()) {

            System.err.println("Register failed: Invalid input");
            return false;
        }

        // 2. Check username / email tồn tại chưa
        if (userRepository.existsByUsername(username)) {
            System.err.println("Register failed: Username already exists");
            return false;
        }

        if (userRepository.existsByEmail(email)) {
            System.err.println("Register failed: Email already exists");
            return false;
        }
        // 3. Hash mật khẩu

        String hashedPassword = PasswordUtils.hash(password);
        //PasswordUtils.hash(password) || "";

        // 4. Tạo user mới
        User newUser = new User(
            java.util.UUID.randomUUID().toString(),
            username.trim(),
            email.trim(),
            hashedPassword
        );

        // 5. Lưu vào MongoDB
        boolean success = userRepository.register(newUser);

        if (success) {
            System.out.println("User registered: " + username);
        }

        return success;
    }

    
    /**
     * Đăng nhập
     */
    public boolean login(String username, String password) {
        User user = userRepository.authenticate(username, password);
        if (user != null) {
            this.currentUser = user;
            System.out.println("User logged in: " + username);
            return true;
        }
        System.err.println("Login failed for: " + username);
        return false;
    }
    
    /**
     * Đăng xuất
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("User logged out: " + currentUser.getUsername());
            this.currentUser = null;
        }
    }
    
    /**
     * Kiểm tra đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Lấy user hiện tại
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Lấy userId hiện tại
     */
    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    // ==================== TODO MANAGEMENT ====================
    
    /**
     * Lấy toàn bộ tasks của user hiện tại
     */
    public List<TodoItem> getAll() {
        if (!isLoggedIn()) {
            System.err.println("No user logged in!");
            return List.of(); // Trả về list rỗng
        }
        return todoRepository.loadByUserId(getCurrentUserId());
    }

    /**
     * Thêm task cho user hiện tại
     */
    public void addTask(String title) {
        if (!isLoggedIn()) {
            System.err.println("Cannot add task: No user logged in!");
            return;
        }
        
        if (title == null || title.trim().isEmpty()) return;

        TodoItem newItem = new TodoItem(title.trim());
        todoRepository.add(newItem, getCurrentUserId());
        System.out.println("Task added for user " + currentUser.getUsername() + ": " + title);
    }

    /**
     * Thêm task với ngày cho user hiện tại
     */
    public void addTask(String title, LocalDate date) {
        if (!isLoggedIn()) {
            System.err.println("Cannot add task: No user logged in!");
            return;
        }
        
        TodoItem item = new TodoItem(title, date);
        todoRepository.add(item, getCurrentUserId());
        System.out.println("Task with date added for user " + currentUser.getUsername() + ": " + title);
    }
    
    /**
     * Thêm task với ngày và createdBy
     */
    public void addTask(String title, LocalDate date, String createdBy) {
        if (!isLoggedIn()) {
            System.err.println("Cannot add task: No user logged in!");
            return;
        }
        
        TodoItem item = new TodoItem(title, date, createdBy);
        todoRepository.add(item, getCurrentUserId());
        System.out.println("Task added for user " + currentUser.getUsername() + ": " + title);
    }

    /**
     * Xoá task (chỉ của user hiện tại)
     */
    public void removeTask(TodoItem item) {
        if (!isLoggedIn()) {
            System.err.println("Cannot remove task: No user logged in!");
            return;
        }
        
        boolean removed = todoRepository.remove(item.getId(), getCurrentUserId());
        if (removed) {
            System.out.println("Task removed: " + item.getTitle());
        } else {
            System.err.println("Cannot remove task: Not found or not owned by user");
        }
    }

    /**
     * Đánh dấu đã xong (chỉ của user hiện tại)
     */
    public void toggleCompleted(TodoItem item) {
        if (!isLoggedIn()) {
            System.err.println("Cannot toggle task: No user logged in!");
            return;
        }
        
        item.setCompleted(!item.isCompleted());
        boolean updated = todoRepository.updateById(
            item.getId(), 
            item, 
            getCurrentUserId()
        );
        
        if (updated) {
            System.out.println("Task updated: " + item.getTitle() + 
                              " (completed: " + item.isCompleted() + ")");
        } else {
            System.err.println("Cannot update task: Not found or not owned by user");
        }
    }

    /**
     * Tìm task theo ID (của user hiện tại)
     */
    public TodoItem getTaskById(String id) {
        if (!isLoggedIn()) return null;
        
        TodoItem item = todoRepository.getById(id);
        // Kiểm tra xem task có thuộc về user hiện tại không
        if (item != null && item.getUserId().equals(getCurrentUserId())) {
            return item;
        }
        return null;
    }
    
    /**
     * Đếm tasks của user hiện tại
     */
    public long countTasks() {
        if (!isLoggedIn()) return 0;
        return todoRepository.countByUserId(getCurrentUserId());
    }
    
    /**
     * Tìm tasks đã/chưa hoàn thành (của user hiện tại)
     */
    public List<TodoItem> getTasksByCompletion(boolean completed) {
        if (!isLoggedIn()) return List.of();
        return todoRepository.findByCompletedAndUserId(completed, getCurrentUserId());
    }
    
    /**
     * Lấy tasks theo ngày (của user hiện tại)
     */
    public List<TodoItem> getTasksByDate(LocalDate date) {
        return getAll().stream()
            .filter(item -> item.getDate().equals(date))
            .collect(Collectors.toList());
    }

    /**
     * Lấy tasks theo tháng (của user hiện tại)
     */
    public List<TodoItem> getTasksByMonth(YearMonth yearMonth) {
        return getAll().stream()
            .filter(item -> {
                LocalDate itemDate = item.getDate();
                return itemDate.getYear() == yearMonth.getYear() 
                    && itemDate.getMonthValue() == yearMonth.getMonthValue();
            })
            .collect(Collectors.toList());
    }

    /**
     * Update vào database (MongoDB auto-saves)
     */
    public void save() {
        todoRepository.save();
    }
    
    // ==================== ADMIN METHODS (Optional) ====================
    
    /**
     * Lấy tất cả users (chỉ dành cho admin)
     */
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
    
    /**
     * Xóa user (chỉ dành cho admin)
     */
    public boolean deleteUser(String userId) {
        return userRepository.delete(userId);
    }
}