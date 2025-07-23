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
import java.util.HashSet;
import java.util.Set;

public class MuteCommand implements CommandExecutor {
    // Map: playerName -> muteEndTimestamp (0 = permanent)
    public static final Set<String> mutedPlayers = new HashSet<>();
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
                mutedPlayers.add(name);
            }
        }
    }

    public static void saveMutes() {
        mutedConfig.set("muted", null);
        for (String name : mutedPlayers) {
            mutedConfig.set("muted." + name, true);
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
        String adminTag = org.utils.utils.utils.MessageUtils.getAdminPermissionTag();
        if (!player.isOp() && !player.getScoreboardTags().contains(adminTag)) {
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
        String targetName = target.getName().toLowerCase();
        mutedPlayers.add(targetName);
        saveMutes();
        MessageUtils.sendMessage(player, "&aMuted &e" + target.getName() + "&a.");
        MessageUtils.sendMessage(target, "&cYou have been muted by a moderator.");
        return true;
    }
}