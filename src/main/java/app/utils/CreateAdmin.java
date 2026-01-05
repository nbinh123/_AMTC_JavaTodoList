package app.utils;

import app.model.User;
import app.model.UserMongoRepository;

public class CreateAdmin {

    public static void createDefaultAdmin() {
        UserMongoRepository userRepo = new UserMongoRepository();

        String username = "admin";
        String email = "admin@gmail.com";
        String password = "admin123"; // test thôi

        // Nếu admin đã tồn tại → không tạo nữa
        if (userRepo.existsByUsername(username)) {
            System.out.println("Admin user already exists");
            return;
        }

        User admin = new User(
            username,
            password,
            email
        );

        boolean success = userRepo.register(admin);

        if (success) {
            System.out.println("Admin user created successfully");
        } else {
            System.out.println("Failed to create admin user");
        }
    }
}
