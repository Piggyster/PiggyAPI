package me.piggyster.api.config;

import me.piggyster.api.color.ColorAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HandledMessage {

    private String message;

    public HandledMessage(String message) {
        this.message = message;
    }

    public HandledMessage() {
        this("");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HandledMessage setPlaceholder(String placeholder, Object value) {
        message = message.replace(placeholder, value.toString());
        return this;
    }

    public String getMessage() {
        return ColorAPI.process(message);
    }

    public void send(Player player) {
        player.sendMessage(getMessage());
    }

    public void send(CommandSender sender) {
        sender.sendMessage(getMessage());
    }


    public void send(Collection<Player> players) {
        for(Player player : players) {
            send(player);
        }
    }

    public void send(Player[] players) {
        for(Player player : players) {
            send(player);
        }
    }

    public HandledMessage clone() {
        return new HandledMessage(message);
    }
}
