package io.ib67.ucf;

import io.ib67.ucf.matcher.PatternMatcher;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.ProxySelector;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BukkitLoader extends JavaPlugin {
    {
        getDataFolder().mkdirs();
        saveDefaultConfig();
        reloadConfig();
        getLogger().info("UpdateCheckFucker by @iceBear67");
        var config = new BlockerConfig(
                getConfig().getBoolean("onlyMainThread"),
                new PatternMatcher(getConfig().getStringList("url").stream().map(Pattern::compile).collect(Collectors.toList())),
                getConfig().getBoolean("verbose")
        );
        if (!(ProxySelector.getDefault() instanceof DelegatedProxySelector)) {
            ProxySelector.setDefault(new DelegatedProxySelector(ProxySelector.getDefault(), config));
        }
        getLogger().info("UpdateCheck Fucker loaded.");
    }
}
