package org.utils.utils.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PermissionUtils {

    /**
     * Check if a player has the specified tag
     */
    public static boolean hasTag(Player player, String tag) {
        switch (tag.toLowerCase()) {
            case "mod":
                return player.hasPermission("cherlsonia.mod") || player.isOp();
            case "admin":
                return player.hasPermission("cherlsonia.admin") || player.isOp();
            case "admin+":
                return player.hasPermission("cherlsonia.adminplus") || player.isOp();
            case "owner2":
                return player.hasPermission("cherlsonia.owner2") || player.isOp();
            default:
                return false;
        }
    }

    /**
     * Check if a player can access the admin menu
     */
    public static boolean canAccessAdminMenu(Player player) {
        return hasTag(player, "admin") || hasTag(player, "admin+") || hasTag(player, "owner2") || player.isOp();
    }

    /**
     * Check if a player can access the moderator menu
     */
    public static boolean canAccessModMenu(Player player) {
        return hasTag(player, "mod") 
                || hasTag(player, "admin") 
                || hasTag(player, "admin+") || hasTag(player, "owner2")
                || player.isOp();
    }

    /**
     * Check if a player can manage operators
     */
    public static boolean canManageOperators(Player player) {
        return hasTag(player, "owner2") || player.isOp();
    }

    /**
     * Returns the permission level of a player
     */
    public static String getPlayerPermissionLevel(Player player) {
        if (hasTag(player, "owner2"))
            return "Owner2";
        if (hasTag(player, "admin+"))
            return "Admin+";
        if (hasTag(player, "admin"))
            return "Admin";
        if (hasTag(player, "mod"))
            return "Moderator";
        return "Player";
    }

    /**
     * Add a tag (permission) to a player
     */
    public static void addTag(Player player, String tag) {
        String perm = getPermissionFromTag(tag);
        if (perm != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " permission set " + perm + " true");
        }
        if (tag.equalsIgnoreCase("admin+")) {
            player.setOp(true);
        }
    }

    /**
     * Remove a tag (permission) from a player
     */
    public static void removeTag(Player player, String tag) {
        String perm = getPermissionFromTag(tag);
        if (perm != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " permission set " + perm + " false");
        }
        if (tag.equalsIgnoreCase("admin+")) {
            player.setOp(false);
        }
    }

    /**
     * Helper - convert tag to permission string
     */
    private static String getPermissionFromTag(String tag) {
        switch (tag.toLowerCase()) {
            case "mod": return "cherlsonia.mod";
            case "admin": return "cherlsonia.admin";
            case "admin+": return "cherlsonia.adminplus";
            case "owner2": return "cherlsonia.owner2";
            default: return null;
        }
    }
}
