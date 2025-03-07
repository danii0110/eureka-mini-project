package dao;

import common.DBManager;
import dto.Phone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneDao {
    // 새로운 휴대폰 등록
    public int createPhone(Phone phone) {
        int res = -1;
        String sql = "INSERT INTO phone (brand, model, price, stock) VALUES (?,?,?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, phone.getBrand());
            pstmt.setString(2, phone.getModel());
            pstmt.setInt(3, phone.getPrice());
            pstmt.setInt(4, phone.getStock());

            res = pstmt.executeUpdate();
            con.commit(); // 명시적 커밋
            System.out.println("[SUCCESS] 휴대폰 등록 완료: " + phone.getBrand() + " " + phone.getModel());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback(); // 예외 발생 시 롤백
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return res;
    }

    // 휴대폰 정보 수정
    public int updatePhone(Phone phone) {
        int res = -1;
        String sql = "UPDATE phone SET brand = ?, model = ?, price = ?, stock = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, phone.getBrand());
            pstmt.setString(2, phone.getModel());
            pstmt.setInt(3, phone.getPrice());
            pstmt.setInt(4, phone.getStock());
            pstmt.setInt(5, phone.getId());

            res = pstmt.executeUpdate();
            con.commit(); // 명시적 커밋
            System.out.println("[SUCCESS] 휴대폰 수정 완료 - ID: " + phone.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback(); // 예외 발생 시 롤백
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return res;
    }

    // 휴대폰 삭제
    public int deletePhone(int id) {
        int res = -1;
        String sql = "DELETE FROM phone WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            res = pstmt.executeUpdate();
            con.commit(); // 명시적 커밋
            System.out.println("[SUCCESS] 휴대폰 삭제 완료 - ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return res;
    }

    // 특정 휴대폰 조회
    public Phone getPhoneById(int id) {
        Phone phone = null;
        String sql = "SELECT * FROM phone WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                phone = new Phone(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("price"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return phone;
    }

    // 전체 휴대폰 목록 조회
    public List<Phone> getAllPhones() {
        List<Phone> phones = new ArrayList<>();
        String sql = "SELECT * FROM phone";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                phones.add(new Phone(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return phones;
    }

    // 판매 가능한(재고가 있는) 휴대폰 목록 조회 (사용자용)
    public List<Phone> getAvailablePhones() {
        List<Phone> phones = new ArrayList<>();
        String sql = "SELECT * FROM phone WHERE stock > 0";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                phones.add(new Phone(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return phones;
    }

    // 특정 휴대폰의 재고 수량 변경
    public int updateStock(int phoneId, int newStock) {
        int res = -1;
        String sql = "UPDATE phone SET stock = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, phoneId);

            res = pstmt.executeUpdate();
            con.commit(); // 명시적 커밋
            System.out.println("[SUCCESS] 재고 수정 완료 - ID: " + phoneId);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return res;
    }
}