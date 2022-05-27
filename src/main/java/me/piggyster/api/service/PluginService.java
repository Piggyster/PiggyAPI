package me.piggyster.api.service;

import org.bukkit.plugin.java.JavaPlugin;

public interface PluginService<P extends JavaPlugin> {

    void initialize();

    P getPlugin();

}
