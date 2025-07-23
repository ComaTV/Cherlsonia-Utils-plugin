package org.utils.utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;

public class TpacceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "player_only");
            return true;
        }
        Player target = (Player) sender;
        java.util.UUID targetId = target.getUniqueId();
        java.util.UUID requesterId = org.utils.utils.commands.TpaCommand.tpaRequests.remove(targetId);
        if (requesterId == null) {
            MessageUtils.sendMessage(target, "&cYou have no pending tpa requests.");
            return true;
        }
        Player requester = target.getServer().getPlayer(requesterId);
        if (requester == null || !requester.isOnline()) {
            MessageUtils.sendMessage(target, "&cThe player who requested to teleport is no longer online.");
            return true;
        }
        requester.teleport(target.getLocation());
        MessageUtils.sendMessage(requester, "&aYour tpa request was accepted! Teleporting...");
        MessageUtils.sendMessage(target, "&aYou accepted the tpa request from &e" + requester.getName() + "&a.");
        return true;
    }
}