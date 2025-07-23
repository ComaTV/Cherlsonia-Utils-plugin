package org.utils.utils.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.utils.utils.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prompt = JavaPlugin.getProvidingPlugin(getClass()).getConfig().getString("help_prompt");
        if (prompt != null) {
            for (String line : prompt.split("\\n")) {
                MessageUtils.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', line));
            }
        } else {
            MessageUtils.sendMessage(sender, "&cNo help prompt configured.");
        }
        return true;
    }
}