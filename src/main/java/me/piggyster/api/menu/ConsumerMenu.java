package me.piggyster.api.menu;

import me.piggyster.api.PiggyAPIPlugin;
import me.piggyster.api.color.ColorAPI;
import me.piggyster.api.menu.item.MenuItem;
import me.piggyster.api.menu.item.SimpleItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ConsumerMenu implements Listener {


    protected ConfigurationSection section;
    protected Player player;
    protected Inventory inventory;
    protected Map<Integer, Consumer<InventoryClickEvent>> consumers;

    public ConsumerMenu(Player player, FileConfiguration config, String configPath) {
        section = config.getConfigurationSection(configPath);
        this.player = player;
    }

    public ConsumerMenu(Player player) {
        this.player = player;
        Bukkit.getServer().getPluginManager().registerEvents(this, PiggyAPIPlugin.getInstance());
    }

    /*public Map<Integer, MenuItem> getItemsFromConfig() {
        Map<Integer, MenuItem> map = new HashMap<>();
        ConfigurationSection itemSection = section.getConfigurationSection("items");
        itemSection.getKeys(false).forEach(key -> {
        
        });
        return map;
    }

    public MenuItem getItemFromName(String name) {
        ConfigurationSection section = this.section.getConfigurationSection("items." + name);
        return new MenuItem(SimpleItem.fromSection(section)).setSlot(section.getInt("slot"));
    }*/

    public void clear() {
        inventory.clear();
        consumers.clear();
    }

    public void fillEmpty(SimpleItem item) {
        while(inventory.firstEmpty() != -1) {
            inventory.setItem(inventory.firstEmpty(), item.build());
        }
    }

    public void setItem(SimpleItem item, int[] slots) {
        for(int slot : slots) {
            setItem(item, slot);
        }
    }

    public void setItem(SimpleItem item, List<Integer> slots) {
        for(int slot : slots) {
            setItem(item, slot);
        }
    }


    public void setItem(SimpleItem item, int slot, Consumer<InventoryClickEvent> consumer) {
        setItem(item.build(), slot, consumer);
    }

    public void setItem(SimpleItem item, int slot) {
        setItem(item.build(), slot);
    }

    public void setItem(ItemStack item, int slot, Consumer<InventoryClickEvent> consumer) {
        consumers.put(slot, consumer);
        setItem(item, slot);
    }

    public void setItem(ItemStack item, int slot) {
        inventory.setItem(slot, item);
    }

    public void setTempItem(SimpleItem item, int slot, long delay) {
        setTempItem(item.build(), slot, delay);
    }

    public void setTempItem(ItemStack item, int slot, long delay) {
        ItemStack currentItem = inventory.getItem(slot);
        Consumer<InventoryClickEvent> consumer = consumers.get(slot);
        consumers.remove(slot);
        inventory.setItem(slot, item);
        Bukkit.getScheduler().runTaskLater(PiggyAPIPlugin.getInstance(), () -> {
            inventory.setItem(slot, currentItem);
            if(consumer != null) consumers.put(slot, consumer);
        }, delay);
    }



    public abstract void draw();

    public abstract int getSize();

    public abstract String getTitle();

    public void open() {
        consumers = new HashMap<>();
        inventory = Bukkit.createInventory(player, getSize(), ColorAPI.process(getTitle()));
        draw();
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() != null && event.getClickedInventory().equals(inventory)) {
            event.setCancelled(true);
            Consumer<InventoryClickEvent> consumer = consumers.get(event.getSlot());
            if(consumer == null) return;
            consumer.accept(event);
        }
    }

}
