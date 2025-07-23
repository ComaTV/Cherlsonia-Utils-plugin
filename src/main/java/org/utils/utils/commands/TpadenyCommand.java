package org.utils.utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;

public class TpadenyCommand implements CommandExecutor {
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
        if (requester != null && requester.isOnline()) {
            MessageUtils.sendMessage(requester, "&cYour tpa request was denied by &e" + target.getName() + "&c.");
        }
        MessageUtils.sendMessage(target, "&aYou denied the tpa request.");
        return true;
    }
}