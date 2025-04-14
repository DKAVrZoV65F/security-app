package com.student.data;

import com.student.db.DBConnection;
import com.student.model.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceRepository {

    public static void addResource(Resource resource) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO resources(name, type, owner) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getType());
            stmt.setString(3, resource.getOwner());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                resource.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Resource> getAllResources() {
        List<Resource> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM resources")) {
            while (rs.next()) {
                Resource res = new Resource(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("owner")
                );
                res.setId(rs.getInt("id"));
                list.add(res);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static Resource getByName(String name) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM resources WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Resource res = new Resource(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("owner")
                );
                res.setId(rs.getInt("id"));
                return res;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void updateResource(Resource resource) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE resources SET name = ?, type = ?, owner = ? WHERE id = ?")) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getType());
            stmt.setString(3, resource.getOwner());
            stmt.setInt(4, resource.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteResource(int resourceId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM resources WHERE id = ?")) {
            stmt.setInt(1, resourceId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}