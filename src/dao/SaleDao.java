package dao;

import common.DBManager;
import dto.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDao {
    // 휴대폰 구매 (판매 등록, 재고 감소 포함)
    public int createSale(int userId, int phoneId) {
        int res = -1;
        String insertSaleSql = "INSERT INTO sale (user_id, phone_id) VALUES (?, ?)";
        String decreaseStockSql = "UPDATE phone SET stock = stock - 1 WHERE id = ? AND stock > 0";

        Connection con = null;
        PreparedStatement saleStmt = null;
        PreparedStatement stockStmt = null;

        try {
            con = DBManager.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            stockStmt = con.prepareStatement(decreaseStockSql); // 재고 감소
            stockStmt.setInt(1, phoneId);
            int stockUpdated = stockStmt.executeUpdate();

            if (stockUpdated > 0) { // 재고가 충분할 때만 판매 등록
                saleStmt = con.prepareStatement(insertSaleSql);
                saleStmt.setInt(1, userId);
                saleStmt.setInt(2, phoneId);
                res = saleStmt.executeUpdate();
            }

            con.commit(); // 트랜잭션 커밋
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback(); // 롤백
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(saleStmt, con);
            DBManager.releaseConnection(stockStmt, null);
        }

        return res;
    }

    // 특정 판매 내역 조회 (phone 테이블 조인 추가)
    public Sale getSaleById(int saleId) {
        Sale sale = null;
        String sql = "SELECT s.id, s.user_id, s.phone_id, s.sale_date, p.brand, p.model, p.price " +
                "FROM sale s " +
                "JOIN phone p ON s.phone_id = p.id " +
                "WHERE s.id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, saleId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sale = new Sale(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("phone_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("price"),
                        rs.getTimestamp("sale_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return sale;
    }

    // 특정 사용자의 구매 내역 조회 (JOIN 추가)
    public List<Sale> getSalesByUserId(int userId) {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT s.id, s.user_id, s.phone_id, s.sale_date, p.brand, p.model, p.price " +
                "FROM sale s " +
                "JOIN phone p ON s.phone_id = p.id " +
                "WHERE s.user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sales.add(new Sale(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("phone_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("price"),
                        rs.getTimestamp("sale_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return sales;
    }

    // 전체 판매 내역 조회 (관리자용) - phone 테이블 JOIN 추가
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT s.id, s.user_id, s.phone_id, s.sale_date, p.brand, p.model, p.price " +
                "FROM sale s " +
                "JOIN phone p ON s.phone_id = p.id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sales.add(new Sale(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("phone_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("price"),
                        rs.getTimestamp("sale_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, con);
        }

        return sales;
    }
}