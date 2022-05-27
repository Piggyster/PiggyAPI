package me.piggyster.api.menu;

import com.google.common.collect.Lists;
import me.piggyster.api.menu.item.MenuItem;
import me.piggyster.api.menu.item.SimpleItem;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PaginatedMenu extends ConsumerMenu {

    private AtomicInteger page;

    public PaginatedMenu(Player owner) {
        this(owner, 1);
    }

    public PaginatedMenu(Player owner, int page) {
        super(owner);
        this.page = new AtomicInteger(page);
    }

    public abstract List<Integer> getSlots();

    public abstract List<MenuItem> getContent();

    public abstract int getNextPageSlot();

    public abstract int getPreviousPageSlot();

    public abstract SimpleItem getNextPageItem();

    public abstract SimpleItem getPreviousPageItem();

    public int getCurrentPage() {
        return page.get();
    }

    public int getMaxPage() {
        return Lists.partition(getContent(), getSlots().size()).size();
    }

    public void draw() {
        clear();
        List<List<MenuItem>> pages = Lists.partition(getContent(), getSlots().size());

        if(page.get() < 1) {
            page.set(1);
        } else if(page.get() > pages.size()) {
            page.set(Math.max(1, pages.size()));
        }

        List<MenuItem> page = pages.isEmpty() ? new ArrayList<>() : pages.get(this.page.get() - 1);
        AtomicInteger i = new AtomicInteger(0);
        getSlots().forEach(slot -> {
            if(page.size() <= i.get()) return;
            MenuItem item = page.get(i.get());
            setItem(item.getItem(), slot, item.getConsumer());
            i.incrementAndGet();
        });
        setItem(getNextPageItem(), getNextPageSlot(), (event) -> {
            if(this.page.get() >= getMaxPage()) return;
            this.page.incrementAndGet();
            draw();
        });
        setItem(getPreviousPageItem(), getPreviousPageSlot(), (event) -> {
            if(this.page.get() <= 1) return;
            this.page.decrementAndGet();
            draw();
        });
    }
}
