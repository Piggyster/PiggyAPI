package me.piggyster.api;

import me.piggyster.api.menu.item.SimpleItem;
import me.piggyster.api.menu.item.SimpleMaterial;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class PiggyAPIPlugin extends JavaPlugin  {

    private static PiggyAPIPlugin instance;

    private final static Pattern hex = Pattern.compile("<#([0-9A-Fa-f]{6})>");
    private final static Pattern glow = Pattern.compile("<GLOW:#([0-9A-Fa-f]{6}),#([0-9A-Fa-f]{6})>(.*?)</GLOW>");
    private final static Pattern gradient1 = Pattern.compile("<GRADIENT:#([0-9A-Fa-f]{6}),#([0-9A-Fa-f]{6})>(.*?)</GRADIENT>");
    private final static Pattern gradient2 = Pattern.compile("<GRADIENT:#([0-9A-Fa-f]{6})>(.*?)</GRADIENT:#([0-9A-Fa-f]{6})>");

    private final static Pattern regex = Pattern.compile("Lv ([0-9]+) - [+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)k blocks - [+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)([km]) Orbs");

    
    public void onEnable() {
        instance = this;
    }

    public void onDisable() {
        instance = null;
    }

    public static PiggyAPIPlugin getInstance() {
        return instance;
    }
}
