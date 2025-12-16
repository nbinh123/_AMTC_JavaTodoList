package app.ulti;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Hash mật khẩu mới
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Kiểm tra mật khẩu
    public static boolean check(String plainPassword, String hashed) {
        return BCrypt.checkpw(plainPassword, hashed);
    }
}
