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
    
    public void add(TodoItem item) {
        try {
            collection.insertOne(item);
            System.out.println("Added todo: " + item.getTitle() + " (ID: " + item.getId() + ")");
        } catch (Exception e) {
            System.err.println("Error adding todo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean remove(TodoItem item) {
        try {
            DeleteResult result = collection.deleteOne(Filters.eq("_id", item.getId()));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error removing todo: " + e.getMessage());
            return false;
        }
    }
    
    public void update(int index, TodoItem newItem) {
        List<TodoItem> items = load();
        if (index >= 0 && index < items.size()) {
            TodoItem oldItem = items.get(index);
            updateById(oldItem.getId(), newItem);
        }
    }
    
    public List<TodoItem> getAll() {
        return load();
    }
    
    public void save() {
        System.out.println("MongoDB: Auto-save enabled");
    }
    
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
    
    public TodoItem getById(String id) {
        try {
            return collection.find(Filters.eq("_id", id)).first();
        } catch (Exception e) {
            System.err.println("Error getting todo by ID: " + e.getMessage());
            return null;
        }
    }
    
    // THÊM METHOD NÀY - đã thiếu
    public List<TodoItem> findByCompleted(boolean completed) {
        List<TodoItem> items = new ArrayList<>();
        try {
            collection.find(Filters.eq("completed", completed)).into(items);
        } catch (Exception e) {
            System.err.println("Error finding by completed: " + e.getMessage());
        }
        return items;
    }
    
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting todos: " + e.getMessage());
            return 0;
        }
    }
}
