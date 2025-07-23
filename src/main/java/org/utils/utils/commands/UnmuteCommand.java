package org.utils.utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "player_only");
            return true;
        }
        Player player = (Player) sender;
        String adminTag = org.utils.utils.utils.MessageUtils.getAdminPermissionTag();
        if (!player.isOp() && !player.getScoreboardTags().contains(adminTag)) {
            MessageUtils.sendMessage(player, "operator_only");
            return true;
        }
        if (args.length != 1) {
            MessageUtils.sendMessage(player, "&cUsage: /unmute <player>");
            return true;
        }
        String targetName = args[0].toLowerCase();
        boolean removed = org.utils.utils.commands.MuteCommand.mutedPlayers.remove(targetName);
        org.utils.utils.commands.MuteCommand.saveMutes();
        if (removed) {
            MessageUtils.sendMessage(player, "&aUnmuted &e" + args[0] + "&a.");
        } else {
            MessageUtils.sendMessage(player, "&cPlayer was not muted.");
        }
        return true;
    }
}