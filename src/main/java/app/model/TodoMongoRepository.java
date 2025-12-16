package app.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import app.database.MongoDBConnection;

public class TodoMongoRepository {
    
    private final MongoCollection<TodoItem> collection;
    
    public TodoMongoRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("todos", TodoItem.class);
        System.out.println("MongoDB TodoRepository initialized");
    }
    
    // ==================== LEGACY METHODS (không dùng userId) ====================
    // Giữ lại để backward compatible, nhưng nên dùng các method mới
    
    /**
     * @deprecated Use loadByUserId(String userId) instead
     */
    @Deprecated
    public List<TodoItem> load() {
        List<TodoItem> items = new ArrayList<>();
        try {
            collection.find().into(items);
            System.out.println("Loaded " + items.size() + " todos from MongoDB");
        } catch (Exception e) {
            System.err.println("Error loading from MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * @deprecated Use add(TodoItem item, String userId) instead
     */
    @Deprecated
    public void add(TodoItem item) {
        try {
            collection.insertOne(item);
            System.out.println("Added todo: " + item.getTitle() + " (ID: " + item.getId() + ")");
        } catch (Exception e) {
            System.err.println("Error adding todo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * @deprecated Use remove(String todoId, String userId) instead
     */
    @Deprecated
    public boolean remove(TodoItem item) {
        try {
            DeleteResult result = collection.deleteOne(Filters.eq("_id", item.getId()));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error removing todo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @deprecated Use updateById(String id, TodoItem newItem, String userId) instead
     */
    @Deprecated
    public boolean updateById(String id, TodoItem newItem) {
        try {
            Bson update = combine(
                set("title", newItem.getTitle()),
                set("completed", newItem.isCompleted())
            );
            
            UpdateResult result = collection.updateOne(Filters.eq("_id", id), update);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error updating todo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @deprecated Use loadByUserId(String userId) instead
     */
    @Deprecated
    public List<TodoItem> getAll() {
        return load();
    }
    
    /**
     * @deprecated Use findByCompletedAndUserId(boolean completed, String userId) instead
     */
    @Deprecated
    public List<TodoItem> findByCompleted(boolean completed) {
        List<TodoItem> items = new ArrayList<>();
        try {
            collection.find(Filters.eq("completed", completed)).into(items);
        } catch (Exception e) {
            System.err.println("Error finding by completed: " + e.getMessage());
        }
        return items;
    }
    
    // ==================== NEW METHODS (có userId) ====================
    
    /**
     * Lấy tất cả todos của một user cụ thể
     */
    public List<TodoItem> loadByUserId(String userId) {
        List<TodoItem> items = new ArrayList<>();
        try {
            collection.find(Filters.eq("user_id", userId)).into(items);
            System.out.println("Loaded " + items.size() + " todos for user: " + userId);
        } catch (Exception e) {
            System.err.println("Error loading todos for user: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * Thêm todo cho một user cụ thể
     */
    public void add(TodoItem item, String userId) {
        try {
            item.setUserId(userId);  // Gán userId vào item
            collection.insertOne(item);
            System.out.println("Added todo for user " + userId + ": " + item.getTitle() + " (ID: " + item.getId() + ")");
        } catch (Exception e) {
            System.err.println("Error adding todo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Xóa todo (kiểm tra ownership)
     * @param todoId ID của todo cần xóa
     * @param userId ID của user đang thực hiện
     * @return true nếu xóa thành công
     */
    public boolean remove(String todoId, String userId) {
        try {
            // Filter: phải match cả _id VÀ user_id
            Bson filter = Filters.and(
                Filters.eq("_id", todoId),
                Filters.eq("user_id", userId)
            );
            
            DeleteResult result = collection.deleteOne(filter);
            boolean deleted = result.getDeletedCount() > 0;
            
            if (deleted) {
                System.out.println("Removed todo: " + todoId + " for user: " + userId);
            } else {
                System.err.println("Cannot remove todo: not found or not owned by user");
            }
            
            return deleted;
        } catch (Exception e) {
            System.err.println("Error removing todo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update todo (kiểm tra ownership)
     * @param id ID của todo cần update
     * @param newItem Todo mới chứa data cần update
     * @param userId ID của user đang thực hiện
     * @return true nếu update thành công
     */
    public boolean updateById(String id, TodoItem newItem, String userId) {
        try {
            // Filter: phải match cả _id VÀ user_id
            Bson filter = Filters.and(
                Filters.eq("_id", id),
                Filters.eq("user_id", userId)
            );
            
            // Update các fields
            Bson update = combine(
                set("title", newItem.getTitle()),
                set("completed", newItem.isCompleted()),
                set("date", newItem.getDate()),  // Nếu có field date
                set("created_by", newItem.getCreatedBy())  // Nếu có field createdBy
            );
            
            UpdateResult result = collection.updateOne(filter, update);
            boolean updated = result.getModifiedCount() > 0;
            
            if (updated) {
                System.out.println("Updated todo: " + id + " for user: " + userId);
            } else {
                System.err.println("Cannot update todo: not found or not owned by user");
            }
            
            return updated;
        } catch (Exception e) {
            System.err.println("Error updating todo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tìm todos theo trạng thái completed cho một user
     */
    public List<TodoItem> findByCompletedAndUserId(boolean completed, String userId) {
        List<TodoItem> items = new ArrayList<>();
        try {
            Bson filter = Filters.and(
                Filters.eq("completed", completed),
                Filters.eq("user_id", userId)
            );
            collection.find(filter).into(items);
            System.out.println("Found " + items.size() + " " + 
                             (completed ? "completed" : "incomplete") + 
                             " todos for user: " + userId);
        } catch (Exception e) {
            System.err.println("Error finding by completed: " + e.getMessage());
        }
        return items;
    }
    
    /**
     * Lấy todo theo ID (không kiểm tra ownership - dùng cho internal)
     */
    public TodoItem getById(String id) {
        try {
            return collection.find(Filters.eq("_id", id)).first();
        } catch (Exception e) {
            System.err.println("Error getting todo by ID: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy todo theo ID và userId (có kiểm tra ownership)
     */
    public TodoItem getById(String id, String userId) {
        try {
            Bson filter = Filters.and(
                Filters.eq("_id", id),
                Filters.eq("user_id", userId)
            );
            return collection.find(filter).first();
        } catch (Exception e) {
            System.err.println("Error getting todo by ID: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Đếm todos của một user
     */
    public long countByUserId(String userId) {
        try {
            long count = collection.countDocuments(Filters.eq("user_id", userId));
            System.out.println("User " + userId + " has " + count + " todos");
            return count;
        } catch (Exception e) {
            System.err.println("Error counting todos: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Đếm tất cả todos (dùng cho admin)
     */
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting todos: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Update method (legacy - không dùng)
     */
    @Deprecated
    public void update(int index, TodoItem newItem) {
        List<TodoItem> items = load();
        if (index >= 0 && index < items.size()) {
            TodoItem oldItem = items.get(index);
            updateById(oldItem.getId(), newItem);
        }
    }
    
    /**
     * Save method - MongoDB auto-saves
     */
    public void save() {
        System.out.println("MongoDB: Auto-save enabled");
    }
    
    // ==================== ADMIN METHODS ====================
    
    /**
     * Xóa tất cả todos của một user (dùng khi xóa user)
     */
    public long deleteAllByUserId(String userId) {
        try {
            DeleteResult result = collection.deleteMany(Filters.eq("user_id", userId));
            long deleted = result.getDeletedCount();
            System.out.println("Deleted " + deleted + " todos for user: " + userId);
            return deleted;
        } catch (Exception e) {
            System.err.println("Error deleting todos for user: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Lấy tất cả todos (dùng cho admin)
     */
    public List<TodoItem> getAllTodos() {
        return load();
    }
}