package app.dao;

import common.DBManager;
import dao.UserDao;
import dto.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {
    private UserDao userDao;
    private Connection con;

    @BeforeAll
    void setUp() throws SQLException {
        userDao = new UserDao();
        con = DBManager.getConnection();
        con.setAutoCommit(false); // 트랜잭션 시작(자동 커밋 비활성화)
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (con != null) {
            con.rollback();
            con.close();
        }
    }

    @Test
    @DisplayName("회원 가입 성공")
    void createUser_Success() {
        // Given
        User user = new User(0, "홍길동", "010-1234-5678", "test@example.com", "password123", new Timestamp(System.currentTimeMillis()), "user");

        // When
        int res = userDao.createUser(user);

        // Then
        assertEquals(1, res);
    }

    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void getUserByEmail_Success() {
        // Given
        String email = "test@example.com";

        // Then
        User user = userDao.getUserByEmail(email);

        // When
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    @DisplayName("전체 사용자 목록 조회 성공")
    void getAllUsers_Success() {
        // Given
        User user = new User(0, "김철수", "010-5678-1234", "chulsoo@example.com", "password456", new Timestamp(System.currentTimeMillis()), "user");
        userDao.createUser(user);

        // When
        List<User> users = userDao.getAllUsers();

        // Then
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }
}