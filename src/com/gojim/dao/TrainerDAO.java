package com.gojim.dao;

import com.gojim.config.DatabaseConnection;
import com.gojim.model.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TrainerDAO {

    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();

        String sql = "SELECT id, full_name, specialty, experience FROM trainers ORDER BY id DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                trainers.add(new Trainer(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialty"),
                        rs.getString("experience")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainers;
    }

    public boolean addTrainer(String fullName, String specialty, String experience) {
        String sql = "INSERT INTO trainers (full_name, specialty, experience) VALUES (?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, specialty);
            stmt.setString(3, experience);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTrainer(int trainerId) {
        String sql = "DELETE FROM trainers WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trainerId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTrainer(int id, String fullName, String specialty, String experience) {
        String sql = "UPDATE trainers SET full_name = ?, specialty = ?, experience = ? WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, specialty);
            stmt.setString(3, experience);
            stmt.setInt(4, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalTrainers() {
        String sql = "SELECT COUNT(*) FROM trainers";

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