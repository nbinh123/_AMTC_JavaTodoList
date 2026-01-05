package app.model;

import app.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.set;


import app.database.MongoDBConnection;

public class UserMongoRepository {
    
    private final MongoCollection<User> collection;
    
    public UserMongoRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("users", User.class);
        System.out.println("MongoDB UserRepository initialized");
    }
    
    // Đăng ký user mới
    public boolean register(User user) {
        try {
            // Kiểm tra username đã tồn tại chưa
            if (findByUsername(user.getUsername()) != null) {
                System.err.println("Username already exists");
                return false;
            }
            
            collection.insertOne(user);
            System.out.println("User registered: " + user.getUsername());
            return true;
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    // Tìm user theo username
    public User findByUsername(String username) {
        try {
            return collection.find(Filters.eq("username", username)).first();
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
            return null;
        }
    }

    public boolean existsByUsername(String username) {
        try {
            return collection.find( Filters.eq("username", username)).first() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsByEmail(String email) {
        try {
            return collection.find(Filters.eq("email", email)).first() != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Tìm user theo email
    public User findByEmail(String email) {
        try {
            return collection.find(Filters.eq("email", email)).first();
        } catch (Exception e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
    }
    
    // Xác thực đăng nhập
    public User authenticate(String username, String password) {
        try {
            User user = findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                // Cập nhật last login
                updateLastLogin(user.getId());
                return user;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            return null;
        }
    }
    
    // Cập nhật last login
    private void updateLastLogin(ObjectId userId) {
        try {
            collection.updateOne(
                Filters.eq("_id", userId),
                set("last_login", new Date())
            );
        } catch (Exception e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }

    // Lấy tất cả users
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            collection.find().into(users);
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
    
    // Xóa user
    public boolean delete(String userId) {
        try {
            return collection.deleteOne(Filters.eq("_id", userId))
                    .getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // Đếm số users
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting users: " + e.getMessage());
            return 0;
        }
    }
    public void createAdminIfNotExists() {

        if (existsByUsername("admin")) {
            System.out.println("Admin already exists");
            return;
        }

        User admin = new User(
            "admin",
            "admin123",           // mật khẩu test
            "admin@todo.local"
        );

        collection.insertOne(admin);
        System.out.println("✅ Admin user created: admin / admin123");
    }

}
