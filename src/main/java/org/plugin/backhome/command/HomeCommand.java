package org.plugin.backhome.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.backhome.database.DatabaseManager;

public class HomeCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final DatabaseManager db;

    public HomeCommand(JavaPlugin plugin, DatabaseManager db) {
        this.plugin = plugin;
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用该指令！");
            return true;
        }
        Player p = (Player) sender;
        Location home = db.getHome(p.getUniqueId());
        if (home == null) {
            p.sendMessage("§c你还没有设置家！请先使用 /sethome");
            return true;
        }
        p.sendMessage("§a3 秒后传送回家...");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (p.isOnline()) {
                    p.teleport(home);
                    p.sendMessage("§a欢迎回家！");
                }
            }
        }.runTaskLater(plugin, 60L);
        return true;
    }
}