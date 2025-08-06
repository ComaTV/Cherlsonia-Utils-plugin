package org.utils.utils.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.utils.utils.Main;

public class CombatIndicator {
    private static final Map<UUID, BukkitRunnable> chatTimers = new HashMap<>();

    public static void showCombatBar(Player player, String opponentName) {
    }

    public static void hideCombatBar(Player player) {
    }

    public static void sendCombatActionBar(Player player, String message) {
        player.sendActionBar(message);
    }

    public static void clearCombatActionBar(Player player) {
        player.sendActionBar("");
    }

    public static void showCombatStartMessage(Player player, String opponentName) {
        sendCombatActionBar(player, "§c⚔ COMBAT STARTED ⚔");
    }

    public static void showCombatEndMessage(Player player) {
        sendCombatActionBar(player, "§a⚔ COMBAT ENDED ⚔");

        new BukkitRunnable() {
            @Override
            public void run() {
                clearCombatActionBar(player);
            }
        }.runTaskLater(Main.getInstance(), 60L);
    }

    public static void showCombatLogoutWarning(Player player) {
        sendCombatActionBar(player, "§c⚠ COMBAT LOGOUT WARNING ⚠");
    }

    public static void clearAllBars() {
    }

    public static void startChatTimer(Player player, String opponentName) {
        UUID playerId = player.getUniqueId();

        stopChatTimer(player);

        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !CombatManager.isInCombat(player)) {
                    stopChatTimer(player);
                    return;
                }

                long timeRemaining = CombatManager.getCombatTimeRemaining(player);
                int secondsLeft = (int) (timeRemaining / 1000);

                if (secondsLeft > 0) {
                    String timerMessage;
                    if (secondsLeft <= 5) {
                        timerMessage = "§c⚔ Combat Timer: " + secondsLeft + "s ⚔";
                    } else if (secondsLeft <= 10) {
                        timerMessage = "§e⚔ Combat Timer: " + secondsLeft + "s ⚔";
                    } else {
                        timerMessage = "§7⚔ Combat Timer: " + secondsLeft + "s ⚔";
                    }

                    sendCombatActionBar(player, timerMessage);
                } else {
                    stopChatTimer(player);
                    if (CombatManager.isInCombat(player)) {
                        CombatManager.endCombat(player.getUniqueId());
                    }
                }
            }
        };

        chatTimers.put(playerId, timer);
        timer.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

    public static void stopChatTimer(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitRunnable timer = chatTimers.remove(playerId);
        if (timer != null) {
            timer.cancel();
        }

        clearCombatActionBar(player);
    }

    public static void clearAllTimers() {
        for (BukkitRunnable timer : chatTimers.values()) {
            timer.cancel();
        }
        chatTimers.clear();
    }
}