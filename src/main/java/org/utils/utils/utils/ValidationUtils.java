package org.utils.utils.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ValidationUtils {
    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}