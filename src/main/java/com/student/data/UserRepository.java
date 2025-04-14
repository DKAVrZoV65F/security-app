package com.student.data;

import com.student.db.DBConnection;
import com.student.model.Role;
import com.student.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public static User addUser(String username, String password, Role role, boolean activated) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users(username, passwordHash, role, activated) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, hash);
            stmt.setString(3, role.name());
            stmt.setBoolean(4, activated);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        User user = new User(username, hash, role);
        user.setActivated(activated);
        return user;
    }

    public static User addUser(String username, String password, Role role) {
        return addUser(username, password, role, false);
    }

    public static void updateUserRole(String username, Role newRole) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET role = ? WHERE username = ?")) {
            stmt.setString(1, newRole.name());
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateUserActivation(String username, boolean activated) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET activated = ? WHERE username = ?")) {
            stmt.setBoolean(1, activated);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static User findByUsername(String username) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String passwordHash = rs.getString("passwordHash");
                Role role = Role.valueOf(rs.getString("role"));
                boolean activated = rs.getBoolean("activated");
                User user = new User(username, passwordHash, role);
                user.setActivated(activated);
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                String username = rs.getString("username");
                String passwordHash = rs.getString("passwordHash");
                Role role = Role.valueOf(rs.getString("role"));
                boolean activated = rs.getBoolean("activated");
                User user = new User(username, passwordHash, role);
                user.setActivated(activated);
                list.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void deleteUser(String username) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
