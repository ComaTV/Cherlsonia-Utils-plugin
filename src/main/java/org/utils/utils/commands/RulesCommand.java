package org.utils.utils.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.utils.utils.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class RulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String rules = JavaPlugin.getProvidingPlugin(getClass()).getConfig().getString("rules");
        if (rules != null) {
            for (String line : rules.split("\\n")) {
                MessageUtils.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', line));
            }
        } else {
            MessageUtils.sendMessage(sender, "&cNo rules configured.");
        }
        return true;
    }
}