package app.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    
    // Hash password khi đăng ký
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }
    
    // Verify password khi đăng nhập
    public static boolean verify(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Password verification error: " + e.getMessage());
            return false;
        }
    }
}