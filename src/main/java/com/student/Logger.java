package com.student;

import com.student.db.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    public static void log(String message) {

        String logMsg = "[" + LocalDateTime.now() + "] " + message;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO logs(message) VALUES (?)")) {
            stmt.setString(1, logMsg);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(logMsg);
    }

    public static List<String> getLogEntries() {
        List<String> logs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT message FROM logs ORDER BY id DESC")) {
            while (rs.next()) {
                logs.add(rs.getString("message"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return logs;
    }
}