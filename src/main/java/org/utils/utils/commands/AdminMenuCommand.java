package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.utils.utils.utils.MessageUtils;
import org.utils.utils.utils.PermissionUtils;

import java.util.Arrays;

public class AdminMenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        boolean isOwner2 = player.getScoreboardTags().contains("Owner2");
        boolean isAdminPlus = player.getScoreboardTags().contains("admin+");
        boolean isOp = player.isOp();
        boolean isAdmin = player.getScoreboardTags().contains("admin");
        boolean isMod = player.getScoreboardTags().contains("mod");
        boolean isForceGhost = player.getScoreboardTags().contains("ForceGhost");
        if (!isOwner2 && !isAdminPlus && !isOp && !isAdmin && !isMod && !isForceGhost) {
            MessageUtils.sendMessage(player, "§cYou don't have permissions to access the admin menu!");
            return true;
        }

        openAdminMenu(player);
        return true;
    }

    public static void openAdminMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "§8Admin Menu");

        ItemStack vanish = createItem(Material.POTION, "§bVanish", "§7We use super vanish plugin");
        ItemStack spectator = createItem(Material.ENDER_EYE, "§aSpectator Mode", "§7Toggle spectator mode");
        ItemStack survival = createItem(Material.GRASS_BLOCK, "§eSurvival Mode", "§7Toggle survival mode");
        ItemStack creative = createItem(Material.BEDROCK, "§eCreative Mode", "§7Toggle creative mode");
        ItemStack teleport = createItem(Material.COMPASS, "§aTeleport to Players", "§7Teleport to any online player");
        ItemStack playerInfo = createItem(Material.BOOK, "§eView Player Info", "§7View detailed information about players");
        ItemStack homes = createItem(Material.BOOK, "Homes", "§7View detailed information about Homes");
        ItemStack waypoints = createItem(Material.BOOK, "Waypoints", "§7View detailed information about Waypoints");
        ItemStack claims = createItem(Material.DIRT, "§6Manage Claims", "§7Delete claims, transfer claims,", "§7set global claim price and whatnot");
        ItemStack muteMenu = createItem(Material.STICK, "§dMute Players", "§7Quick mute menu for players", "§7Select player and mute duration");
        ItemStack kick = createItem(Material.IRON_AXE, "§6Kick Players", "§7Kick players from the server");
        ItemStack ban = createItem(Material.DIAMOND_SWORD, "§4Ban Players", "§7Ban players from the server");
        ItemStack operators = createItem(Material.REDSTONE_TORCH, "§cAll Current Operators and Admin Tags", "§7Show everyone with admin tag", "§7with op and with the \"mod\" tag");
        ItemStack enderChests = createItem(Material.ENDER_CHEST, "§5View Ender Chests", "§7Shows all online players");
        ItemStack inventory = createItem(Material.CHEST, "§fView Inventory", "§7Shows all online players");

        boolean isOwner2 = player.getScoreboardTags().contains("Owner2");
        boolean isAdminPlus = player.getScoreboardTags().contains("admin+");
        boolean isOp = player.isOp();
        boolean isAdmin = player.getScoreboardTags().contains("admin");
        boolean isMod = player.getScoreboardTags().contains("mod");
        boolean isForceGhost = player.getScoreboardTags().contains("ForceGhost");

        if (isOwner2 || isAdminPlus || isOp) {
            menu.setItem(0, vanish);
            menu.setItem(18, spectator);
            menu.setItem(19, survival);
            menu.setItem(20, creative);
            menu.setItem(22, teleport);
            menu.setItem(10, claims);
            menu.setItem(3, muteMenu);
            menu.setItem(12, operators);
            menu.setItem(5, enderChests);
            menu.setItem(14, inventory);
            menu.setItem(15, playerInfo);
            menu.setItem(16, kick);
            menu.setItem(17, ban);
            menu.setItem(25, homes);
            menu.setItem(26, waypoints);
        } else if (isAdmin) {
            menu.setItem(0, vanish);
            menu.setItem(18, spectator);
            menu.setItem(19, survival);
            menu.setItem(20, creative);
            menu.setItem(22, teleport);
            menu.setItem(3, muteMenu);
            menu.setItem(5, enderChests);
            menu.setItem(14, inventory);
            menu.setItem(15, playerInfo);
            menu.setItem(16, kick);
            menu.setItem(17, ban);
            menu.setItem(25, homes);
            menu.setItem(26, waypoints);
        } else if (isMod) {
            menu.setItem(3, muteMenu);
            menu.setItem(5, enderChests);
            menu.setItem(14, inventory);
            menu.setItem(16, kick);
        } else if (isForceGhost) {
            menu.setItem(18, spectator);
            menu.setItem(19, survival);
            menu.setItem(20, creative);
        }

        player.openInventory(menu);
    }

    private static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
