package me.piggyster.api.menu.item;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class MenuItem {

    private int slot;
    private SimpleItem item;
    private Consumer<InventoryClickEvent> consumer;

    public MenuItem(SimpleItem item) {
        this.item = item;
    }

    public MenuItem() {}

    public MenuItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public int getSlot() {
        return slot;
    }

    public SimpleItem getItem() {
        return item;
    }

    public MenuItem setConsumer(Consumer<InventoryClickEvent> consumer) {
        this.consumer = consumer;
        return this;
    }

    public Consumer<InventoryClickEvent> getConsumer() {
        return consumer;
    }
}
