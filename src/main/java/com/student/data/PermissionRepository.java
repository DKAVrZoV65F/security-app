package com.student.data;

import com.student.db.DBConnection;
import com.student.model.Permission;
import com.student.model.Role;

import java.sql.*;

public class PermissionRepository {

    public static void grantPermission(Permission permission) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO permissions(resourceName, role, canRead, canEdit, canDelete) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, permission.getResourceName());
            stmt.setString(2, permission.getRole().name());
            stmt.setBoolean(3, permission.canRead());
            stmt.setBoolean(4, permission.canEdit());
            stmt.setBoolean(5, permission.canDelete());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Permission getPermission(String resourceName, Role role) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM permissions WHERE resourceName=? AND role=?")) {
            stmt.setString(1, resourceName);
            stmt.setString(2, role.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Permission(
                        resourceName,
                        role,
                        rs.getBoolean("canRead"),
                        rs.getBoolean("canEdit"),
                        rs.getBoolean("canDelete")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}