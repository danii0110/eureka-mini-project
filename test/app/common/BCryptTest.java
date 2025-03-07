package app.common;

import common.PasswordUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BCryptTest {

    @Test
    public void testPasswordHashingAndVerification() {
        // Given (테스트용 비밀번호)
        String rawPassword = "securePassword123";

        // When (비밀번호 해싱)
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        // Then (비밀번호 검증)
        assertNotNull(hashedPassword, "해싱된 비밀번호가 null이면 안 됩니다.");
        assertNotEquals(rawPassword, hashedPassword, "해싱된 비밀번호는 원본과 달라야 합니다.");
        assertTrue(PasswordUtil.checkPassword(rawPassword, hashedPassword), "비밀번호 검증이 실패했습니다.");
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hashedPassword), "잘못된 비밀번호가 검증에 성공하면 안 됩니다.");
    }
}