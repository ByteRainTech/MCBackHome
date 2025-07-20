package org.plugin.backhome;

import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.backhome.database.DatabaseManager;
import org.plugin.backhome.command.SetHomeCommand;
import org.plugin.backhome.command.HomeCommand;
import org.plugin.backhome.command.CityCommand;

public class BackHome extends JavaPlugin {
    private DatabaseManager db;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        db = new DatabaseManager(this);
        db.init();

        getCommand("sethome").setExecutor(new SetHomeCommand(this, db));
        getCommand("home").setExecutor(new HomeCommand(this, db));
        getCommand("city").setExecutor(new CityCommand(this));

        getLogger().info("BackHome 插件已启用！");
    }

    @Override
    public void onDisable() {
        db.close();
        getLogger().info("BackHome 插件已关闭！");
    }

    public DatabaseManager getDatabase() {
        return db;
    }
}