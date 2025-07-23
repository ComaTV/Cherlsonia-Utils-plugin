package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;

public class AdminTpCommand implements CommandExecutor {
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
        if (args.length != 1) {
            MessageUtils.sendMessage(player, "&cUsage: /admintp <player>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(player, "&cPlayer not found or not online.");
            return true;
        }
        player.teleport(target.getLocation());
        MessageUtils.sendMessage(player, "&aTeleported to &e" + target.getName() + "&a.");
        return true;
    }
}