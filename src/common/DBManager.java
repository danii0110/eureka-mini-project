package common;

import java.sql.*;

public class DBManager {
    static String url = "jdbc:mysql://localhost:3306/phone_sales";
    static String user = "root";
    static String pwd = "0000";

    // DB 연결 메서드
    public static Connection connection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, pwd);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    // DB 연결 해제(PreparedStatement & Connection)
    public static void releaseConnection(PreparedStatement pstmt, Connection con) {
        try {
            pstmt.close();
            con.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // DB 연결 해제(ResultSet 포함)
    public static void releaseConnection(ResultSet rs, PreparedStatement pstmt, Connection con) {
        try {
            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}