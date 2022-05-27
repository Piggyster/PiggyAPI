package me.piggyster.api.config;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HandledSound {

    private Sound sound;
    private float volume;
    private float pitch;


    public HandledSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public HandledSound(Sound sound) {
        this(sound, 1f, 1f);
    }

    public HandledSound clone() {
        return new HandledSound(sound, volume, pitch);
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void play(Collection<Player> players) {
        for(Player player : players) {
            play(player);
        }
    }

    public void play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }



}
