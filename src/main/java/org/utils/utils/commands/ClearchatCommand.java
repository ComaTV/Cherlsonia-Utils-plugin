package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.utils.utils.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class ClearchatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)
                || (!((Player) sender).isOp() && !((Player) sender).getScoreboardTags().contains("mod"))) {
            MessageUtils.sendMessage(sender, "operator_only");
            return true;
        }
        String msg = JavaPlugin.getProvidingPlugin(getClass()).getConfig().getString("clearchat_message",
                "&aChat has been cleared!");
        for (int i = 0; i < 100; i++) {
            for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("");
            }
        }
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            MessageUtils.sendMessage(p, msg);
        }
        MessageUtils.sendMessage(sender, "&aYou cleared the chat.");
        return true;
    }
}