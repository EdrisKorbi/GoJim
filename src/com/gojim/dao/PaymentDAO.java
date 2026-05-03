package com.gojim.dao;

import com.gojim.config.DatabaseConnection;
import com.gojim.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();

        String sql = """
                SELECT p.id, m.full_name, p.amount, p.payment_date, p.status
                FROM payments p
                JOIN members m ON p.member_id = m.id
                ORDER BY p.id DESC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getDouble("amount"),
                        rs.getString("payment_date"),
                        rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return payments;
    }

    public boolean addPayment(int memberId, double amount, String status) {
        String sql = """
                INSERT INTO payments (member_id, amount, payment_date, status)
                VALUES (?, ?, CURDATE(), ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setDouble(2, amount);
            stmt.setString(3, status);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePaymentStatus(int paymentId, String status) {
        String updatePaymentSql = "UPDATE payments SET status = ? WHERE id = ?";
        String updateMemberSql = """
                UPDATE members
                SET status = ?
                WHERE id = (
                    SELECT member_id FROM payments WHERE id = ?
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updatePaymentSql)) {
                stmt.setString(1, status);
                stmt.setInt(2, paymentId);
                stmt.executeUpdate();
            }

            String memberStatus = status.equals("Paid") ? "Active" : "Pending";

            try (PreparedStatement stmt = conn.prepareStatement(updateMemberSql)) {
                stmt.setString(1, memberStatus);
                stmt.setInt(2, paymentId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payments WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'Paid'";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTodayPaidVisits() {
        String sql = "SELECT COUNT(*) FROM payments WHERE status = 'Paid' AND payment_date = CURDATE()";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getPendingRevenue() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'Pending'";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}