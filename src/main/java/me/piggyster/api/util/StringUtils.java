package me.piggyster.api.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class StringUtils {

    public static String getProgressBar(int barCount, double current, double max) {
        double percentage = current / max;
        int greenBars = (int) Math.floor(percentage * barCount);
        String output = "&a";
        for(int i = 0; i < greenBars; i++) {
            output = output + "|";
        }
        int remaining = barCount - greenBars;
        for(int i = 0; i < remaining; i++) {
            if(i == 0) output = output + "|";
            output = output + "|";
        }
        return output;
    }

    public static String entityToString(EntityType type) {
        return WordUtils.capitalizeFully(type.toString().replace("_", " "));
    }

    public static String materialToString(Material material) {
        return WordUtils.capitalizeFully(material.toString().replace("_", " "));
    }
}
