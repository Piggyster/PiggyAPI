package me.piggyster.api.menu;

import me.piggyster.api.color.ColorAPI;
import me.piggyster.api.config.ConfigManager;
import me.piggyster.api.menu.item.SimpleItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements Listener {

    protected Player owner;
    protected Inventory inventory;
    private Plugin plugin;
    protected ConfigManager configManager;
    protected Map<Integer, String> keys;

    public Menu(JavaPlugin plugin, Player owner) {
        this(plugin, owner, null);
    }

    protected Menu(JavaPlugin plugin, Player owner, ConfigManager configManager) {
        this.owner = owner;
        this.configManager = configManager;
        this.plugin = plugin;
        keys = new HashMap<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public abstract void formatItems();

    public abstract int getSize();

    public abstract String getTitle();


    public abstract void onClick(InventoryClickEvent event);

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        if(event.getInventory().equals(inventory)) {
            onClick(event);
        }
    }

    public void clear() {
        inventory.clear();
        keys.clear();
    }

    public void fillEmpty(SimpleItem item) {
        while(inventory.firstEmpty() != -1) {
            inventory.setItem(inventory.firstEmpty(), item.build());
        }
    }

    public void setItem(SimpleItem item, String key, int slot) {
        setItem(item.build(), key, slot);
    }

    public void setItem(ItemStack item, String key, int slot) {
        if(key != null) {
            keys.put(slot, key);
        }
        inventory.setItem(slot, item);
    }

    public void setTempItem(SimpleItem item, int slot, long delay) {
        ItemStack currentItem = inventory.getItem(slot);
        String value = keys.get(slot);
        keys.remove(slot);
        inventory.setItem(slot, item.build());
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(slot, currentItem);
                keys.put(slot, value);
            }
        }.runTaskLater(plugin, delay);
    }

    public void addItem(SimpleItem item, String key, int slot) {
        if(key != null) {
            keys.put(slot, key);
        }
        inventory.setItem(slot, item.build());
    }

    public void open() {
        inventory = Bukkit.createInventory(null, getSize(), ColorAPI.process(getTitle()));
        keys = new HashMap<>();
        formatItems();
        owner.openInventory(inventory);
    }
}
