package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;
import org.utils.utils.utils.EconomyUtils;
import org.utils.utils.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {
    // Map: target UUID -> requester UUID
    public static final Map<UUID, UUID> tpaRequests = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "player_only");
            return true;
        }
        Player requester = (Player) sender;
        if (args.length != 1) {
            MessageUtils.sendMessage(requester, "&cUsage: /tpa <player>");
            return true;
        }
        int tpaCost = Main.getConfigManager().getTpaCost();
        if (!EconomyUtils.hasMoney(requester, tpaCost)) {
            MessageUtils.sendError(requester, "You do not have enough money to use /tpa! Cost: " + tpaCost);
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(requester, "&cPlayer not found or not online.");
            return true;
        }
        if (target.getUniqueId().equals(requester.getUniqueId())) {
            MessageUtils.sendMessage(requester, "&cYou cannot tpa to yourself!");
            return true;
        }
        EconomyUtils.removeMoney(requester, tpaCost);
        tpaRequests.put(target.getUniqueId(), requester.getUniqueId());
        MessageUtils.sendMessage(requester,
                "&aTpa request sent to &e" + target.getName() + "&a. You have been charged " + tpaCost + ".");
        MessageUtils.sendMessage(target, "&e" + requester.getName()
                + " &ahas requested to teleport to you. Type &b/tpaccept &aor &c/tpadeny&a.");
        return true;
    }
}