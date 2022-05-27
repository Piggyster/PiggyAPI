package me.piggyster.api.command;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    public abstract String getName();

    public abstract void run(CommandSender sender, String[] args);
}
