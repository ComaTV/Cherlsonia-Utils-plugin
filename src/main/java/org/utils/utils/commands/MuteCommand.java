package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.utils.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MuteCommand implements CommandExecutor {
    // Map: playerName -> muteEndTimestamp (0 = permanent)
    public static final Map<String, Long> mutedPlayers = new HashMap<>();
    private static File mutedFile;
    private static YamlConfiguration mutedConfig;

    public static void init(JavaPlugin plugin) {
        mutedFile = new File(plugin.getDataFolder(), "muted.yml");
        if (!mutedFile.exists()) {
            try { mutedFile.createNewFile(); } catch (IOException ignored) {}
        }
        mutedConfig = YamlConfiguration.loadConfiguration(mutedFile);
        loadMutes();
    }

    public static void loadMutes() {
        mutedPlayers.clear();
        if (mutedConfig.contains("muted")) {
            for (String name : mutedConfig.getConfigurationSection("muted").getKeys(false)) {
                long until = mutedConfig.getLong("muted." + name);
                mutedPlayers.put(name, until);
            }
        }
    }

    public static void saveMutes() {
        mutedConfig.set("muted", null);
        for (Map.Entry<String, Long> entry : mutedPlayers.entrySet()) {
            mutedConfig.set("muted." + entry.getKey(), entry.getValue());
        }
        try { mutedConfig.save(mutedFile); } catch (IOException ignored) {}
    }

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
        if (args.length < 1 || args.length > 2) {
            MessageUtils.sendMessage(player, "&cUsage: /mute <player> [minutes]");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(player, "&cPlayer not found or not online.");
            return true;
        }
        String targetName = target.getName().toLowerCase();
        long until = 0;
        if (args.length == 2) {
            try {
                int minutes = Integer.parseInt(args[1]);
                until = System.currentTimeMillis() + minutes * 60_000L;
            } catch (NumberFormatException e) {
                MessageUtils.sendMessage(player, "&cInvalid number of minutes.");
                return true;
            }
        }
        mutedPlayers.put(targetName, until);
        saveMutes();
        if (until == 0) {
            MessageUtils.sendMessage(player, "&aMuted &e" + target.getName() + "&a permanently.");
            MessageUtils.sendMessage(target, "&cYou have been muted permanently by a moderator.");
        } else {
            MessageUtils.sendMessage(player, "&aMuted &e" + target.getName() + "&a for &e" + args[1] + "&a minute(s).");
            MessageUtils.sendMessage(target, "&cYou have been muted for &e" + args[1] + "&c minute(s) by a moderator.");
        }
        return true;
    }
}