package org.utils.utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.CombatManager;
import org.utils.utils.utils.MessageUtils;

public class CombatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (CombatManager.isInCombat(player)) {
                Player opponent = CombatManager.getCombatOpponent(player);
                long timeRemaining = CombatManager.getCombatTimeRemaining(player);

                if (opponent != null) {
                    MessageUtils.sendMessage(player, "&c[Combat] You are in combat with " + opponent.getName() + "!");
                    MessageUtils.sendMessage(player, "&eTime remaining: " + (timeRemaining / 1000) + " seconds");
                } else {
                    MessageUtils.sendMessage(player, "&c[Combat] You are in combat but your opponent is offline!");
                    MessageUtils.sendMessage(player, "&eTime remaining: " + (timeRemaining / 1000) + " seconds");
                }
            } else {
                MessageUtils.sendMessage(player, "&a[Combat] You are not currently in combat.");
            }
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
            if (CombatManager.isInCombat(player)) {
                Player opponent = CombatManager.getCombatOpponent(player);
                long timeRemaining = CombatManager.getCombatTimeRemaining(player);

                MessageUtils.sendMessage(player, "&6=== Combat Status ===");
                MessageUtils.sendMessage(player, "&eStatus: &cIn Combat");
                if (opponent != null) {
                    MessageUtils.sendMessage(player, "&eOpponent: &c" + opponent.getName());
                    MessageUtils.sendMessage(player, "&eOpponent Health: &c" + Math.round(opponent.getHealth()) + "/"
                            + Math.round(opponent.getMaxHealth()));
                } else {
                    MessageUtils.sendMessage(player, "&eOpponent: &cOffline");
                }
                MessageUtils.sendMessage(player, "&eTime Remaining: &c" + (timeRemaining / 1000) + " seconds");
                MessageUtils.sendMessage(player, "&6===================");
            } else {
                MessageUtils.sendMessage(player, "&6=== Combat Status ===");
                MessageUtils.sendMessage(player, "&eStatus: &aNot in Combat");
                MessageUtils.sendMessage(player, "&6===================");
            }
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            MessageUtils.sendMessage(player, "&6=== Combat Commands ===");
            MessageUtils.sendMessage(player, "&e/combat &7- Check your combat status");
            MessageUtils.sendMessage(player, "&e/combat status &7- Show detailed combat information");
            MessageUtils.sendMessage(player, "&e/combat help &7- Show this help message");
            MessageUtils.sendMessage(player, "&6======================");
            return true;
        }

        MessageUtils.sendMessage(player, "&cUnknown command. Use &e/combat help &cfor help.");
        return true;
    }
}