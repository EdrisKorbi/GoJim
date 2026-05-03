package com.gojim.dao;

import com.gojim.config.DatabaseConnection;
import com.gojim.model.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO = Data Access Object.
 * This class handles database operations related to subscriptions.
 */
public class SubscriptionDAO {

    /**
     * Gets all subscription plans from MySQL.
     */
    public List<Subscription> getAllSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();

        String sql = "SELECT id, plan_name, duration_days, price FROM subscriptions";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Subscription subscription = new Subscription(
                        rs.getInt("id"),
                        rs.getString("plan_name"),
                        rs.getInt("duration_days"),
                        rs.getDouble("price"));

                subscriptions.add(subscription);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscriptions;
    }

    /**
     * Adds a new subscription plan to MySQL.
     */
    public boolean addSubscription(String planName, int durationDays, double price) {
        String sql = "INSERT INTO subscriptions (plan_name, duration_days, price) VALUES (?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, planName);
            stmt.setInt(2, durationDays);
            stmt.setDouble(3, price);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubscription(int id, String planName, int durationDays, double price) {
        String sql = "UPDATE subscriptions SET plan_name = ?, duration_days = ?, price = ? WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, planName);
            stmt.setInt(2, durationDays);
            stmt.setDouble(3, price);
            stmt.setInt(4, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubscription(int id) {
        String sql = "DELETE FROM subscriptions WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}