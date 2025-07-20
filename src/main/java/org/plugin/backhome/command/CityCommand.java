package org.plugin.backhome.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CityCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public CityCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用该指令！");
            return true;
        }
        Player p = (Player) sender;

        FileConfiguration cfg = plugin.getConfig();
        String worldName = cfg.getString("city.world");
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            p.sendMessage("§c城市所在的世界不存在！");
            return true;
        }

        Location loc = new Location(
                world,
                cfg.getDouble("city.x"),
                cfg.getDouble("city.y"),
                cfg.getDouble("city.z"),
                (float) cfg.getDouble("city.yaw"),
                (float) cfg.getDouble("city.pitch")
        );

        p.teleport(loc);
        p.sendMessage("§a已传送到城市！");
        return true;
    }
}