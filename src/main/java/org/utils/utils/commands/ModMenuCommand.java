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

public class ModMenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!PermissionUtils.canAccessModMenu(player)) {
            MessageUtils.sendMessage(player, "§cYou don't have permissions to access the moderator menu!");
            return true;
        }

        openModMenu(player);
        return true;
    }

    public static void openModMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "§8Moderator Menu");

        ItemStack mute = createItem(Material.BARRIER, "§cMute Players",
                "§7Mute players for specified duration");

        ItemStack playerInfo = createItem(Material.BOOK, "§eView Player Info",
                "§7View detailed information about players");

        ItemStack teleport = createItem(Material.COMPASS, "§aTeleport to Players",
                "§7Teleport to any online player");

        ItemStack inventories = createItem(Material.CHEST, "§fView Player Inventories",
                "§7View inventories of online players");

        ItemStack enderChests = createItem(Material.ENDER_CHEST, "§5View Player Ender Chests",
                "§7View ender chests of online players");

        ItemStack kick = createItem(Material.IRON_AXE, "§6Kick Players",
                "§7Kick players from the server");

        ItemStack ban = createItem(Material.DIAMOND_SWORD, "§4Ban Players",
                "§7Ban players from the server");

        ItemStack reports = createItem(Material.PAPER, "§bView Reports",
                "§7View player reports and complaints");

        menu.setItem(10, mute);
        menu.setItem(11, playerInfo); 
        menu.setItem(12, teleport); 
        menu.setItem(13, inventories); 
        menu.setItem(14, enderChests); 
        menu.setItem(15, kick); 
        menu.setItem(16, ban); 
        menu.setItem(22, reports);

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
