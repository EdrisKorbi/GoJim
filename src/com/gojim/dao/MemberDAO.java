package com.gojim.dao;

import com.gojim.config.DatabaseConnection;
import com.gojim.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();

        String sql = """
                SELECT m.id, m.full_name, m.age, m.phone,
                       m.subscription_id, s.plan_name,
                       m.start_date, m.end_date,
                       CASE
                            WHEN m.end_date < CURDATE() THEN 'Expired'
                            WHEN EXISTS (
                                SELECT 1 FROM payments p
                                WHERE p.member_id = m.id AND p.status = 'Paid'
                            ) THEN 'Active'
                            ELSE 'Pending'
                        END AS status
                FROM members m
                JOIN subscriptions s ON m.subscription_id = s.id
                ORDER BY m.id DESC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getString("phone"),
                        rs.getInt("subscription_id"),
                        rs.getString("plan_name"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return members;
    }

    public boolean addMember(String fullName, int age, String phone, int subscriptionId) {
        String getSubSql = "SELECT duration_days, price FROM subscriptions WHERE id = ?";

        String insertMemberSql = """
                INSERT INTO members
                (full_name, age, phone, subscription_id, start_date, end_date, status)
                VALUES (?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), 'Pending')
                """;

        String insertPaymentSql = """
                INSERT INTO payments (member_id, amount, payment_date, status)
                VALUES (?, ?, CURDATE(), 'Pending')
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int durationDays;
            double price;

            try (PreparedStatement stmt = conn.prepareStatement(getSubSql)) {
                stmt.setInt(1, subscriptionId);

                ResultSet rs = stmt.executeQuery();

                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }

                durationDays = rs.getInt("duration_days");
                price = rs.getDouble("price");
            }

            int memberId;

            try (PreparedStatement stmt = conn.prepareStatement(insertMemberSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, fullName);
                stmt.setInt(2, age);
                stmt.setString(3, phone);
                stmt.setInt(4, subscriptionId);
                stmt.setInt(5, durationDays);

                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();

                if (!keys.next()) {
                    conn.rollback();
                    return false;
                }

                memberId = keys.getInt(1);
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertPaymentSql)) {
                stmt.setInt(1, memberId);
                stmt.setDouble(2, price);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMember(int memberId) {
        String deletePaymentsSql = "DELETE FROM payments WHERE member_id = ?";
        String deleteMemberSql = "DELETE FROM members WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(deletePaymentsSql)) {
                stmt.setInt(1, memberId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteMemberSql)) {
                stmt.setInt(1, memberId);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMember(int memberId, String fullName, int age, String phone, int subscriptionId) {
        String sql = """
                UPDATE members
                SET full_name = ?,
                    age = ?,
                    phone = ?,
                    subscription_id = ?,
                    end_date = (
                        SELECT DATE_ADD(start_date, INTERVAL duration_days DAY)
                        FROM subscriptions
                        WHERE id = ?
                    )
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setInt(2, age);
            stmt.setString(3, phone);
            stmt.setInt(4, subscriptionId);
            stmt.setInt(5, subscriptionId);
            stmt.setInt(6, memberId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalMembers() {
        String sql = "SELECT COUNT(*) FROM members";

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

    public int getActiveMembersCount() {
        String sql = "SELECT COUNT(*) FROM members WHERE end_date >= CURDATE()";

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
}