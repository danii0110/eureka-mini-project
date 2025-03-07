package common;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // 비밀번호 해싱
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // 비밀번호 검증
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}