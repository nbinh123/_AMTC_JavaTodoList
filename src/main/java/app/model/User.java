package app.model;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class User {

    @BsonId
    private ObjectId id;

    private String username;
    private String email;
    private String password;

    @BsonProperty("created_at")
    private Date createdAt;

    @BsonProperty("last_login")
    private Date lastLogin;

    // ===== CONSTRUCTORS =====
    public User() {
        // Required by MongoDB POJO
    }

    // Dùng cho đăng ký / tạo admin
    public User(String username, String password, String email) {
        this.id = new ObjectId();
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = new Date();
    }

    // ===== GETTERS & SETTERS =====
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIdAsString() {
        return id != null ? id.toHexString() : null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // ⚠️ Sau này sẽ hash
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
