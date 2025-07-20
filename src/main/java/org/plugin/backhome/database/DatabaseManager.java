package org.plugin.backhome.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {
    private final JavaPlugin plugin;
    private Connection conn;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/homes.db");
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS player_homes (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "world TEXT NOT NULL, " +
                    "x REAL NOT NULL, " +
                    "y REAL NOT NULL, " +
                    "z REAL NOT NULL, " +
                    "yaw REAL NOT NULL, " +
                    "pitch REAL NOT NULL)"
                );
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "初始化数据库失败", e);
        }
    }

    public void setHome(UUID uuid, Location loc) {
        String sql = "INSERT OR REPLACE INTO player_homes(uuid, world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, loc.getWorld().getName());
            ps.setDouble(3, loc.getX());
            ps.setDouble(4, loc.getY());
            ps.setDouble(5, loc.getZ());
            ps.setFloat(6, loc.getYaw());
            ps.setFloat(7, loc.getPitch());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "保存家位置失败: " + uuid, e);
        }
    }

    public Location getHome(UUID uuid) {
        String sql = "SELECT world, x, y, z, yaw, pitch FROM player_homes WHERE uuid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Location(
                    Bukkit.getWorld(rs.getString("world")),
                    rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z"),
                    rs.getFloat("yaw"),
                    rs.getFloat("pitch")
                );
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "读取家位置失败: " + uuid, e);
        }
        return null;
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "关闭数据库连接失败", e);
        }
    }
}