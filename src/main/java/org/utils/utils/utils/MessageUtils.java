package org.utils.utils.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.utils.utils.config.ConfigManager;

public class MessageUtils {
    private static ConfigManager configManager;

    public static void setConfigManager(ConfigManager configManager) {
        MessageUtils.configManager = configManager;
    }

    public static void sendMessage(CommandSender sender, String message) {
        if (configManager != null) {
            String prefix = configManager.getMessage("prefix");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static void sendMessage(CommandSender sender, String path, String... replacements) {
        if (configManager != null) {
            String message = configManager.getMessage(path, replacements);
            sendMessage(sender, message);
        } else {
            sendMessage(sender, "Message not found: " + path);
        }
    }

    public static void sendSuccess(CommandSender sender, String message) {
        sendMessage(sender, "&a" + message);
    }

    public static void sendError(CommandSender sender, String message) {
        sendMessage(sender, "&c" + message);
    }

    public static void sendInfo(CommandSender sender, String message) {
        sendMessage(sender, "&e" + message);
    }

    public static String getAdminPermissionTag() {
        if (configManager != null) {
            return configManager.getAdminPermissionTag();
        }
        return "mod";
    }


} 