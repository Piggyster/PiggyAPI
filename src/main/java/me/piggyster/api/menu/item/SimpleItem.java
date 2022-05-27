package me.piggyster.api.menu.item;

import de.tr7zw.nbtapi.NBTItem;
import me.piggyster.api.color.ColorAPI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleItem implements Cloneable {

    private ItemStack itemStack;
    private String skullTexture;

    public SimpleItem(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public SimpleItem(Material material) {
        this(new ItemStack(material));
    }

    public SimpleItem() {
        this(Material.STONE);
    }

    public SimpleItem setMaterial(Material material) {
        if(itemStack == null) {
            itemStack = new ItemStack(material);
        } else {
            itemStack.setType(material);
        }
        return this;
    }

    public SimpleItem setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public SimpleItem setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ColorAPI.process(name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem setLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        lore = lore.stream().map(string -> ColorAPI.process(string)).collect(Collectors.toList());
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem addLore(String lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> currentLore = meta.getLore();
        if(currentLore == null) currentLore = new ArrayList<>();
        currentLore.add(ColorAPI.process(lore));
        meta.setLore(currentLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem addEnchant(Enchantment enchantment) {
        return addEnchant(enchantment, 1);
    }

    public SimpleItem removeEnchant(Enchantment enchantment) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.removeEnchant(enchantment);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem addFlag(ItemFlag flag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(flag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem addFlags(ItemFlag[] flags) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(flags);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem removeFlag(ItemFlag flag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.removeItemFlags(flag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem removeFlags(ItemFlag[] flags) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.removeItemFlags(flags);
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem setGlowing(boolean glowing) {
        if(glowing) {
            addFlag(ItemFlag.HIDE_ENCHANTS);
            addEnchant(Enchantment.LURE, 0);
        } else {
            removeFlag(ItemFlag.HIDE_ENCHANTS);
            removeEnchant(Enchantment.LURE);
        }
        return this;
    }

    public SimpleItem setBase64(String base64) {
        itemStack = SkullCreator.itemWithBase64(itemStack, base64);
        return this;
    }

    public SimpleItem setURL(String url) {
        itemStack = SkullCreator.itemWithUrl(itemStack, url);
        skullTexture = url;
        return this;
    }

    public SimpleItem setSkin(String skin) {
        itemStack = SkullCreator.itemWithBase64(itemStack, skin);
        return this;
    }

    public SimpleItem setArmorColor(Color color) {
        ItemMeta meta = itemStack.getItemMeta();

        if(meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
            armorMeta.setColor(color);
            itemStack.setItemMeta(armorMeta);
        }
        return this;
    }

    public SimpleItem setPlaceholder(String placeholder, Object value) {
        ItemMeta meta = itemStack.getItemMeta();
        if(meta.hasDisplayName()) {
            meta.setDisplayName(meta.getDisplayName().replace(placeholder, ColorAPI.process(value.toString())));
        }
        if(meta.hasLore()) {
            meta.setLore(meta.getLore().stream().map(string -> string.replace(placeholder, ColorAPI.process(value.toString()))).collect(Collectors.toList()));
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public SimpleItem addNBT(String key, Object object) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setObject(key, object);
        itemStack = nbtItem.getItem();
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

    public static SimpleItem fromSection(ConfigurationSection section) {
        SimpleItem simpleItem = new SimpleItem(Material.STONE);
        if(section.isString("material")) {
            simpleItem.setMaterial(Material.valueOf(section.getString("material")));
        }
        if(section.isInt("amount")) {
            simpleItem.setAmount(section.getInt("amount"));
        }
        if(section.isString("name")) {
            simpleItem.setName(section.getString("name"));
        }
        if(section.isList("lore")) {
            simpleItem.setLore(section.getStringList("lore"));
        }
        if(section.isList("enchants")) {
            for(String string : section.getStringList("enchants")) {
                String[] split = string.split(":");
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(split[0].toLowerCase()));
                int level = Integer.parseInt(split[1]);
                simpleItem.addEnchant(enchant, level);
            }
        }
        if(section.isList("flags")) {
            for(String string : section.getStringList("flags")) {
                ItemFlag flag = ItemFlag.valueOf(string.toUpperCase());
                simpleItem.addFlag(flag);
            }
        }
        if(section.isBoolean("glowing")) {
            simpleItem.setGlowing(section.getBoolean("glowing"));
        } else if(section.isBoolean("enchanted")) {
            simpleItem.setGlowing(section.getBoolean("enchanted"));
        }
        if(section.isString("base64")) {
            simpleItem.setBase64(section.getString("base64"));
        }
        if(section.isString("url")) {
            simpleItem.setURL(section.getString("url"));
        }
        if(section.isString("texture")) {
            simpleItem.setURL(section.getString("texture"));
        }
        if(section.isString("skin")) {
            simpleItem.setSkin(section.getString("skin"));
        }
        if(section.isConfigurationSection("armor_color")) {
            ConfigurationSection colorSection = section.getConfigurationSection("armor_color");
            int red = colorSection.getInt("r");
            int green = colorSection.getInt("g");
            int blue = colorSection.getInt("b");
            simpleItem.setArmorColor(Color.fromRGB(red, green, blue));
        }
        return simpleItem;
    }

    public SimpleItem clone() {
        SimpleItem item = new SimpleItem(this.build().clone());
        item.skullTexture = skullTexture;
        return item;
    }


    public Material getMaterial() {
        return itemStack.getType();
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public MenuItem getMenuItem() {
        return new MenuItem(this);
    }

    public SimpleMaterial getSimpleMaterial() {
        if(skullTexture != null && getMaterial() == Material.PLAYER_HEAD) {
            return new SimpleMaterial(skullTexture);
        } else {
            return new SimpleMaterial(getMaterial());
        }
    }

    public SimpleItem setSimpleMaterial(SimpleMaterial material) {
        setMaterial(material.getMaterial());
        if(material.getSkullTexture() != null) setURL(material.getSkullTexture());
        return this;
    }


    public String serialize() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(build());
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch(Exception ex) {
            return null;
        }
    }

    public static SimpleItem deserialize(String base64) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return new SimpleItem(item);
        } catch(Exception ex) {
            return null;
        }
    }



}
