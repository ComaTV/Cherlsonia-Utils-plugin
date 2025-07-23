package org.utils.utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "player_only");
            return true;
        }
        Player player = (Player) sender;
        if (!player.isOp() && !player.getScoreboardTags().contains("mod")) {
            MessageUtils.sendMessage(player, "operator_only");
            return true;
        }
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        MessageUtils.sendMessage(player, "&aYour health has been restored!");
        return true;
    }
}