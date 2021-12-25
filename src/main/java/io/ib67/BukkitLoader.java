package io.ib67;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoader extends JavaPlugin {
    {
        getLogger().info("UpdateCheck Fucker loaded.");
        getDataFolder().mkdirs();
        saveDefaultConfig();
        reloadConfig();

        System.setProperty("ucf.onlyMainThread", String.valueOf(getConfig().getBoolean("onlyMainThread")));
        System.setProperty("ucf.mode",getConfig().getString("matchMode"));
        System.setProperty("ucf.verbose", String.valueOf(getConfig().getBoolean("verbose")));

        UpdateCheckFucker.premain(getDataFolder().toString(),null);
    }
}
