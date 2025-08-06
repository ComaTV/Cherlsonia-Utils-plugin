package org.utils.utils.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.utils.utils.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatManager {
    private static final Map<UUID, CombatSession> combatSessions = new HashMap<>();
    private static int combatTimeoutSeconds = 15;

    public static class CombatSession {
        private final UUID playerId;
        private final UUID opponentId;
        private long lastCombatTime;
        private BukkitRunnable timeoutTask;

        public CombatSession(UUID playerId, UUID opponentId) {
            this.playerId = playerId;
            this.opponentId = opponentId;
            this.lastCombatTime = System.currentTimeMillis();
            startTimeoutTask();
        }

        public void updateCombatTime() {
            this.lastCombatTime = System.currentTimeMillis();
        }

        public void startTimeoutTask() {
            if (timeoutTask != null) {
                timeoutTask.cancel();
            }

            timeoutTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (System.currentTimeMillis() - lastCombatTime >= combatTimeoutSeconds * 1000) {
                        if (combatSessions.containsKey(playerId)) {
                            endCombat(playerId);
                        }
                    }
                }
            };
            timeoutTask.runTaskLater(Main.getInstance(), combatTimeoutSeconds * 20L);
        }

        public void cancelTimeoutTask() {
            if (timeoutTask != null) {
                timeoutTask.cancel();
            }
        }

        public UUID getPlayerId() {
            return playerId;
        }

        public UUID getOpponentId() {
            return opponentId;
        }

        public long getLastCombatTime() {
            return lastCombatTime;
        }
    }

    public static void startCombat(Player player, Player opponent) {
        UUID playerId = player.getUniqueId();
        UUID opponentId = opponent.getUniqueId();

        boolean wasInCombat = isInCombat(player) && getCombatOpponent(player) != null &&
                getCombatOpponent(player).getUniqueId().equals(opponentId);

        silentlyEndCombat(playerId);
        silentlyEndCombat(opponentId);

        CombatSession playerSession = new CombatSession(playerId, opponentId);
        CombatSession opponentSession = new CombatSession(opponentId, playerId);

        combatSessions.put(playerId, playerSession);
        combatSessions.put(opponentId, opponentSession);

        addCombatTag(player);
        addCombatTag(opponent);

        if (!wasInCombat) {
            MessageUtils.sendMessage(player, "&c[Combat] You are now in combat with " + opponent.getName() + "!");
            MessageUtils.sendMessage(opponent, "&c[Combat] You are now in combat with " + player.getName() + "!");

            CombatIndicator.showCombatStartMessage(player, opponent.getName());
            CombatIndicator.showCombatStartMessage(opponent, player.getName());
        }

        CombatIndicator.startChatTimer(player, opponent.getName());
        CombatIndicator.startChatTimer(opponent, player.getName());
    }

    public static void updateCombat(Player player) {
        CombatSession session = combatSessions.get(player.getUniqueId());
        if (session != null) {
            session.updateCombatTime();
            session.startTimeoutTask();
            Player opponent = getCombatOpponent(player);
            if (opponent != null) {
                CombatIndicator.startChatTimer(player, opponent.getName());
            }
        }
    }

    public static void endCombat(UUID playerId) {
        CombatSession session = combatSessions.remove(playerId);
        if (session != null) {
            session.cancelTimeoutTask();

            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                removeCombatTag(player);

                MessageUtils.sendMessage(player, "&a[Combat] Combat has ended!");
                CombatIndicator.showCombatEndMessage(player);
                CombatIndicator.stopChatTimer(player);
            }
        }
    }

    public static void silentlyEndCombat(UUID playerId) {
        CombatSession session = combatSessions.remove(playerId);
        if (session != null) {
            session.cancelTimeoutTask();
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                removeCombatTag(player);
            }
            CombatIndicator.stopChatTimer(player);
        }
    }

    public static boolean isInCombat(Player player) {
        return combatSessions.containsKey(player.getUniqueId());
    }

    public static Player getCombatOpponent(Player player) {
        CombatSession session = combatSessions.get(player.getUniqueId());
        if (session != null) {
            return Bukkit.getPlayer(session.getOpponentId());
        }
        return null;
    }

    public static long getCombatTimeRemaining(Player player) {
        CombatSession session = combatSessions.get(player.getUniqueId());
        if (session != null) {
            long timeElapsed = System.currentTimeMillis() - session.getLastCombatTime();
            long timeRemaining = (combatTimeoutSeconds * 1000) - timeElapsed;
            return Math.max(0, timeRemaining);
        }
        return 0;
    }

    public static void clearAllCombat() {
        for (CombatSession session : combatSessions.values()) {
            session.cancelTimeoutTask();
        }
        combatSessions.clear();
    }

    public static Map<UUID, CombatSession> getCombatSessions() {
        return new HashMap<>(combatSessions);
    }

    public static void loadConfiguration() {
        combatTimeoutSeconds = Main.getConfigManager().getConfig().getInt("combat.timeout_seconds", 15);
    }

    public static int getCombatTimeoutSeconds() {
        return combatTimeoutSeconds;
    }

    private static void addCombatTag(Player player) {
        if (player != null && player.isOnline()) {
            player.addScoreboardTag("combat");

            player.setMetadata("in_combat", new org.bukkit.metadata.FixedMetadataValue(Main.getInstance(), true));
        }
    }

    private static void removeCombatTag(Player player) {
        if (player != null && player.isOnline()) {
            player.removeScoreboardTag("combat");

            player.removeMetadata("in_combat", Main.getInstance());
        }
    }

    public static boolean hasCombatTag(Player player) {
        return player != null && player.getScoreboardTags().contains("combat");
    }

    public static java.util.List<Player> getPlayersInCombat() {
        java.util.List<Player> playersInCombat = new java.util.ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasCombatTag(player)) {
                playersInCombat.add(player);
            }
        }
        return playersInCombat;
    }
}