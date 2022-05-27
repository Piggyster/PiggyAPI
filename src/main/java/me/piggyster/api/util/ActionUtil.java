package me.piggyster.api.util;

import com.google.common.collect.Lists;
import me.piggyster.api.color.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ActionUtil {

    public static void executeActions(Player player, String[] actions) {
        executeActions(player, Lists.newArrayList(actions));
    }

    public static void executeActions(Player player, Collection<String> actions) {
        actions.forEach(action -> executeAction(player, action));
    }


    public static void executeAction(Player player, String action) {
        action = action.replace("%player%", player.getName());
        action = ColorAPI.process(action);

        if (action.startsWith("[noparse]")) {
            action = action.replace("[noparse] ", "");
        } else {
            action = ChatColor.translateAlternateColorCodes('&', action);
        }

        if (action.startsWith("[command]")) {
            action = action.replace("[command] ", "");
            player.performCommand(action);
        }

        if (action.startsWith("[console]")) {
            action = action.replace("[console] ", "");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
        }

        if (action.startsWith("[message]")) {
            action = action.replace("[message] ", "");
            player.sendMessage(action);
        }

        if (action.startsWith("[broadcast]")) {
            action = action.replace("[broadcast] ", "");
            Bukkit.getServer().broadcastMessage(action);
        }

        if(action.startsWith("[economy]")) { //usage: [economy] orbs 100000
            action = action.replace("[economy] ", "");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "bank give " + player.getName() + " " + action);
        }
    }
}
