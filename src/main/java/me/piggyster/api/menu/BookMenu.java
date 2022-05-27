package me.piggyster.api.menu;

import me.piggyster.api.menu.component.SimpleComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public abstract class BookMenu {

    private ItemStack book;
    protected Player owner;

    public BookMenu(Player owner) {
        book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("+");
        meta.setAuthor("+");
        book.setItemMeta(meta);
        this.owner = owner;
        formatComponents();
    }


    public void setPage(SimpleComponent component, int page) {
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.spigot().setPage(page, component.build());
        book.setItemMeta(meta);
    }

    public void addPage(SimpleComponent component) {
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.spigot().addPage(component.build());
        book.setItemMeta(meta);
    }

    public void open() {
        owner.openBook(book);
    }

    public abstract void formatComponents();
}
