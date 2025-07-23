package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;
import java.util.HashSet;
import java.util.Set;

public class MuteCommand implements CommandExecutor {
    public static final Set<String> mutedPlayers = new HashSet<>();

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
            MessageUtils.sendMessage(player, "&cUsage: /mute <player>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(player, "&cPlayer not found or not online.");
            return true;
        }
        mutedPlayers.add(target.getName().toLowerCase());
        MessageUtils.sendMessage(player, "&aMuted &e" + target.getName() + "&a.");
        MessageUtils.sendMessage(target, "&cYou have been muted by a moderator.");
        return true;
    }
}