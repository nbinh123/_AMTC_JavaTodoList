package models;

public class User {
    private String username;
    private String email; 
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; } 
    public boolean checkLogin(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }
}