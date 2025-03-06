package dao;

import common.DBManager;
import dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    // 사용자 생성(회원가입)
    public int createUser(User user) {
        int res = -1;
        String sql = "INSERT INTO user (name, contact, email, password, role) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getContact());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole());

            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return res;
    }

    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM user WHERE email = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return user;
    }
}