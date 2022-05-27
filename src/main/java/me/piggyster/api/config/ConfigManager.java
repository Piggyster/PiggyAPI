package me.piggyster.api.config;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private File dataFolder;
    private JavaPlugin plugin;
    private Map<String, FileConfiguration> configs;
    private Map<String, HandledMessage> messages;
    private Map<String, HandledSound> sounds;
    private String messageConfig;
    private String messageSection;
    private String soundConfig;
    private String soundSection;
    private boolean loadedMessages;
    private boolean loadedSounds;

    public ConfigManager(JavaPlugin plugin, File dataFolder) {
        this.dataFolder = dataFolder;
        this.plugin = plugin;
        configs = new HashMap<>();
        loadedMessages = false;
        loadedSounds = false;
    }

    public ConfigManager(JavaPlugin plugin) {
        this(plugin, plugin.getDataFolder());
    }

    public FileConfiguration createConfig(String name) {
        try {
            if(!dataFolder.exists())
                dataFolder.mkdirs();
            String folderPrefix = (dataFolder.getParentFile() == null || dataFolder == plugin.getDataFolder()) ? "" : dataFolder.getName() + "/";
            File file = new File(dataFolder, name + ".yml");
            boolean isResource = plugin.getResource(folderPrefix + name + ".yml") != null;
            if(!file.exists()) {
                if(isResource) {
                    plugin.saveResource(folderPrefix + name + ".yml", false);
                } else {
                    file.createNewFile();
                }
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            configs.put(name, config);
            return config;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public FileConfiguration getConfig(String name) {
        return configs.get(name);
    }

    public void saveConfig(String name) {
        if(!configs.containsKey(name))
            return;
        try {
            FileConfiguration config = configs.get(name);
            File file = new File(dataFolder, name + ".yml");
            config.save(file);
            config.load(file);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reloadConfig(String name) {
        if(!configs.containsKey(name))
            return;
        try {
            FileConfiguration config = configs.get(name);
            File file = new File(dataFolder, name + ".yml");
            config.load(file);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getDirectoryContent(String directory) {
        List<String> files = new ArrayList<>();
        File file;
        if(directory == null) {
            file = dataFolder;
        } else {
            file = new File(dataFolder, directory);
        }
        if(file.list() == null) return files;
        for(String fileName : file.list()) {
            if(fileName.endsWith(".yml")) {
                files.add(fileName.substring(0, fileName.length() - 4));
            }
        }
        return files;
    }

    public void setMessageConfig(String name) {
        messageConfig = name;
    }

    public void setMessageSection(String section) {
        messageSection = section;
    }

    public HandledMessage getMessage(String message) {
        if(!loadedMessages) {
            reloadMessages();
        }
        if(message == null || !messages.containsKey(message)) {
            return null;
        }
        return messages.get(message).clone();
    }

    public void setSoundConfig(String name) {
        soundConfig = name;
    }

    public void setSoundSection(String section) {
        soundSection = section;
    }

    public HandledSound getSound(String sound) {
        if(!loadedSounds) {
            reloadSounds();
        }
        if(sound == null || !sounds.containsKey(sound)) {
            return null;
        }
        return sounds.get(sound).clone();
    }

    public void reloadAll() {
        for(String config : configs.keySet()) {
            reloadConfig(config);
        }
    }

    public void reloadMessages() {
        messages = new HashMap<>();
        FileConfiguration config = messageConfig == null ? getConfig("config") : getConfig(messageConfig);
        if(config == null)
            return;

        ConfigurationSection section = null;
        if(messageSection != null) {
            section = config.getConfigurationSection(messageSection);
        }
        if(section == null) {
            section = config;
        }

        for(String node : section.getKeys(false)) {
            if(section.isList(node)) {
                List<String> strings = section.getStringList(node);
                messages.put(node, new HandledMessage(String.join("\n", strings)));
            } else {
                messages.put(node, new HandledMessage(section.getString(node)));
            }
        }

        if(!loadedMessages) {
            loadedMessages = true;
        }
    }

    public void reloadSounds() {
        sounds = new HashMap<>();
        FileConfiguration config = soundConfig == null ? getConfig("config") : getConfig(soundConfig);
        if(config == null)
            return;

        ConfigurationSection section = null;
        if(soundSection != null) {
            section = config.getConfigurationSection(soundSection);
        }
        if(section == null) {
            section = config;
        }

        for(String node : section.getKeys(false)) {
            if(!section.isConfigurationSection(node)) continue;
            ConfigurationSection soundSection = section.getConfigurationSection(node);
            if(soundSection == null) continue;
            sounds.put(node, soundFromSection(soundSection));
        }

        if(!loadedSounds) {
            loadedSounds = true;
        }
    }

    private HandledSound soundFromSection(ConfigurationSection section) {
        Sound sound = section.isString("name") ? Sound.valueOf(section.getString("name")) : Sound.BLOCK_NOTE_BLOCK_PLING;
        float volume = section.isDouble("volume") ? (float) section.getDouble("volume") : 1f;
        float pitch = section.isDouble("pitch") ? (float) section.getDouble("pitch") : 1f;
        return new HandledSound(sound, volume, pitch);
    }
}
