package app.model;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class TodoItem {
    @BsonId  // MongoDB sẽ dùng field này làm _id
    private String id;
    
    private String title;
    private boolean completed;
    
    private LocalDate date;
    
    @BsonProperty("created_by")  // Map sang field "created_by" trong MongoDB
    private String createdBy;
    
    @BsonProperty("user_id")  // ✅ THÊM field này - map sang "user_id" trong MongoDB
    private String userId;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor mặc định - MongoDB cần constructor này
     */
    public TodoItem() {
        this.id = java.util.UUID.randomUUID().toString();
        this.date = LocalDate.now();
    }
    
    /**
     * Constructor với title
     */
    public TodoItem(String title) {
        this();
        this.title = title;
    }
    
    /**
     * Constructor với title và completed
     */
    public TodoItem(String title, boolean completed) {
        this();
        this.title = title;
        this.completed = completed;
    }
    
    /**
     * Constructor với title và date
     */
    public TodoItem(String title, LocalDate date) {
        this();
        this.title = title;
        this.date = date;
    }
    
    /**
     * Constructor với title, date và completed
     */
    public TodoItem(String title, LocalDate date, boolean completed) {
        this();
        this.title = title;
        this.date = date;
        this.completed = completed;
    }
    
    /**
     * Constructor với title, date và createdBy
     */
    public TodoItem(String title, LocalDate date, String createdBy) {
        this();
        this.title = title;
        this.date = date;
        this.createdBy = createdBy;
    }
    
    /**
     * ✅ Constructor đầy đủ với userId
     */
    public TodoItem(String title, LocalDate date, String createdBy, String userId) {
        this();
        this.title = title;
        this.date = date;
        this.createdBy = createdBy;
        this.userId = userId;
    }
    
    /**
     * ✅ Constructor đầy đủ tất cả fields
     */
    public TodoItem(String id, String title, boolean completed, LocalDate date, String createdBy, String userId) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.date = date;
        this.createdBy = createdBy;
        this.userId = userId;
    }
    
    // ==================== GETTERS & SETTERS ====================
    
    public String getId() { 
        return id; 
    }
    
    public void setId(String id) { 
        this.id = id; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public boolean isCompleted() { 
        return completed; 
    }
    
    public void setCompleted(boolean completed) { 
        this.completed = completed; 
    }
    
    public LocalDate getDate() { 
        return date; 
    }
    
    public void setDate(LocalDate date) { 
        this.date = date; 
    }
    
    public String getCreatedBy() { 
        return createdBy; 
    }
    
    public void setCreatedBy(String createdBy) { 
        this.createdBy = createdBy; 
    }
    
    /**
     * ✅ Getter cho userId
     */
    public String getUserId() { 
        return userId; 
    }
    
    /**
     * ✅ Setter cho userId
     */
    public void setUserId(String userId) { 
        this.userId = userId; 
    }
    
    // ==================== UTILITY METHODS ====================
    
    @Override
    public String toString() {
        return title + 
               (completed ? " ✓" : " ○") + 
               " [" + date + "]" + 
               (createdBy != null ? " by " + createdBy : "") +
               (userId != null ? " (user: " + userId + ")" : "");
    }
    
    /**
     * ✅ Kiểm tra xem todo này có thuộc về user không
     */
    public boolean belongsTo(String userId) {
        return this.userId != null && this.userId.equals(userId);
    }
    
    /**
     * ✅ Clone todo (hữu ích khi cần copy)
     */
    public TodoItem clone() {
        return new TodoItem(
            this.id,
            this.title,
            this.completed,
            this.date,
            this.createdBy,
            this.userId
        );
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TodoItem other = (TodoItem) obj;
        return id != null && id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}