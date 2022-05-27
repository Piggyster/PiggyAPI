package me.piggyster.api.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command implements CommandExecutor, TabCompleter {

    private Map<String, SubCommand> subCommands;

    public Command(JavaPlugin plugin, String command) {
        plugin.getCommand(command).setExecutor(this);
        subCommands = new HashMap<>();
        plugin.getCommand(command).setTabCompleter(this);
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(args.length > 0 && subCommands.containsKey(args[0])) {
            subCommands.get(args[0]).run(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            run(sender, args);
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(args.length == 1) {
            return Arrays.asList(subCommands.keySet().toArray(new String[]{}));
        }
        return null;
    }

    public abstract void run(CommandSender sender, String[] args);

    public void addSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }
}
