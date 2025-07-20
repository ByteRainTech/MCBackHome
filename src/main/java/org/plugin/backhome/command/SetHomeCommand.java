package org.plugin.backhome.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.backhome.database.DatabaseManager;

public class SetHomeCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final DatabaseManager db;

    public SetHomeCommand(JavaPlugin plugin, DatabaseManager db) {
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
        db.setHome(p.getUniqueId(), p.getLocation());
        p.sendMessage("§a家位置已设置！");
        return true;
    }
}