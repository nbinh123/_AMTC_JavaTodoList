package models;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> listUsers;

    public UserManager() {
        this.listUsers = new ArrayList<>();
        this.listUsers.add(new User("admin", "admin@gmail.com", "123"));
    }

    public boolean register(String username, String email, String password) {
        for (User u : listUsers) {
            if (u.getUsername().equals(username)) return false; 
            if (u.getEmail().equals(email)) return false;       
        }
        
        listUsers.add(new User(username, email, password));
        return true;
    }

    public boolean checkLogin(String username, String password) {
        for (User u : listUsers) {
            if (u.checkLogin(username, password)) return true;
        }
        return false;
    }
}