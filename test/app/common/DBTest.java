package app.common;

import common.DBManager;

import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        // DB 연결 테스트
        Connection con = DBManager.getConnection();

        if (con != null) {
            System.out.println("DB 연결 성공");
        } else {
            System.out.println("DB 연결 실패");
        }

        // 연결 해제 테스트
        DBManager.releaseConnection(null, con);
    }
}
