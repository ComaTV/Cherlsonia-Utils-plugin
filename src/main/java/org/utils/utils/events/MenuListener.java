package org.utils.utils.events;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.utils.utils.commands.AdminMenuCommand;
import org.utils.utils.commands.ModMenuCommand;
import org.utils.utils.utils.MessageUtils;
import org.utils.utils.utils.PermissionUtils;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        // Admin Menu
        if (title.equals("§8Admin Menu")) {
            event.setCancelled(true);
            handleAdminMenuClick(player, clickedItem);
        }
        // Mod Menu
        else if (title.equals("§8Moderator Menu")) {
            event.setCancelled(true);
            handleModMenuClick(player, clickedItem);
        }
        // Mute duration menu
        else if (title.startsWith("§8Mute ")) {
            event.setCancelled(true);
            handleMuteDurationClick(player, clickedItem, title);
        }
        // Ender chest with mute menu
        else if (title.contains(" - Ender Chest + Mute")) {
            event.setCancelled(true);
            handleEnderChestMuteClick(player, clickedItem, title);
        }
        // Ender chest viewer
        else if (title.endsWith("'s Ender Chest")) {
            event.setCancelled(true);
            handleEnderChestViewerClick(player, event, title);
        }
        // Inventory viewer
        else if (title.endsWith("'s Inventory")) {
            event.setCancelled(true);
            handleInventoryViewerClick(player, event, title);
        }
        else if (title.equals("§8Operators & Admin Tags")) {
            event.setCancelled(true);

            if (!(clickedItem.getType() == Material.PLAYER_HEAD)) return;

            String targetName = clickedItem.getItemMeta().getDisplayName().substring(2); // scoate §a
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                MessageUtils.sendMessage(player, "§cPlayer is no longer online!");
                return;
            }

            if (event.isLeftClick()) {
                // deschidem meniul de management pentru acel player
                openRoleManagementMenu(player, target);
            } else if (event.isRightClick()) {
                // afișăm rolurile în chat
                boolean isOp = target.isOp();
                boolean isMod = PermissionUtils.hasTag(target, "mod");
                boolean isAdmin = PermissionUtils.hasTag(target, "admin");
                boolean isAdminPlus = PermissionUtils.hasTag(target, "admin+");

                player.sendMessage("§6=== Roluri pentru §e" + target.getName() + "§6 ===");
                player.sendMessage("§eOperator: " + (isOp ? "§aDa" : "§cNu"));
                player.sendMessage("§eModerator: " + (isMod ? "§aDa" : "§cNu"));
                player.sendMessage("§eAdmin: " + (isAdmin ? "§aDa" : "§cNu"));
                player.sendMessage("§eAdmin+: " + (isAdminPlus ? "§aDa" : "§cNu"));
            }
        }
        else if (title.startsWith("§8Manage Roles - ")) {
            event.setCancelled(true);

            String targetName = title.substring("§8Manage Roles - ".length());
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                MessageUtils.sendMessage(player, "§cPlayer is no longer online!");
                return;
            }

            String itemName = clickedItem.getItemMeta().getDisplayName();

            switch (itemName) {
                case "§aToggle Moderator":
                    if (PermissionUtils.hasTag(target, "mod")) {
                        PermissionUtils.removeTag(target, "mod");
                        MessageUtils.sendMessage(player, "§cAi scos Moderator de la " + target.getName());
                    } else {
                        PermissionUtils.addTag(target, "mod");
                        MessageUtils.sendMessage(player, "§aAi dat Moderator la " + target.getName());
                    }
                    break;

                case "§bToggle Admin":
                    if (PermissionUtils.hasTag(target, "admin")) {
                        PermissionUtils.removeTag(target, "admin");
                        MessageUtils.sendMessage(player, "§cAi scos Admin de la " + target.getName());
                    } else {
                        PermissionUtils.addTag(target, "admin");
                        MessageUtils.sendMessage(player, "§aAi dat Admin la " + target.getName());
                    }
                    break;

                case "§cToggle Admin+":
                    if (PermissionUtils.hasTag(target, "admin+")) {
                        PermissionUtils.removeTag(target, "admin+");
                        target.setOp(false);
                        MessageUtils.sendMessage(player, "§cAi scos Admin+ de la " + target.getName());
                    } else {
                        PermissionUtils.addTag(target, "admin+");
                        target.setOp(true);
                        MessageUtils.sendMessage(player, "§aAi dat Admin+ la " + target.getName());
                    }
                    break;
            }

            // redeschidem meniul ca să vadă modificările
            openRoleManagementMenu(player, target);
        }


    }

    private void handleAdminMenuClick(Player player, ItemStack item) {
        String itemName = item.getItemMeta().getDisplayName();

        switch (itemName) {
            case "§bVanish":
                // Vanish implementation (requires external plugin)
                MessageUtils.sendMessage(player, "§aVanish activated! (Requires SuperVanish plugin)");
                break;

            case "§aSpectator Mode":
                player.setGameMode(GameMode.SPECTATOR);
                MessageUtils.sendMessage(player, "§aSpectator mode activated!");
                break;

            case "§eSurvival Mode":
                player.setGameMode(GameMode.SURVIVAL);
                MessageUtils.sendMessage(player, "§aSurvival mode activated!");
                break;

            case "§6Manage Claims":
                // Claims management implementation
                MessageUtils.sendMessage(player, "§6Claims management menu - in development");
                break;

            case "§dMute Players":
                openPlayerSelectorMenu(player, "mute_only");
                break;

            case "§cAll Current Operators and Admin Tags":
                openOperatorManagementMenu(player);
                break;

            case "§5View Ender Chests":
                openPlayerSelectorMenu(player, "enderchest");
                break;

            case "§fView Inventory":
                openPlayerSelectorMenu(player, "inventory");
                break;
        }
    }

    private void openOperatorManagementMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8Operators & Admin Tags");

        int slot = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (slot >= 54) break;

            boolean isOp = onlinePlayer.isOp();
            boolean isMod = PermissionUtils.hasTag(onlinePlayer, "mod");
            boolean isAdmin = PermissionUtils.hasTag(onlinePlayer, "admin");
            boolean isAdminPlus = PermissionUtils.hasTag(onlinePlayer, "admin+");

            if (isOp || isMod || isAdmin || isAdminPlus) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                org.bukkit.inventory.meta.SkullMeta meta = (org.bukkit.inventory.meta.SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(onlinePlayer);
                meta.setDisplayName("§a" + onlinePlayer.getName());
                head.setItemMeta(meta);

                menu.setItem(slot, head);
                slot++;
            }
        }

        player.openInventory(menu);
    }
    private void handleModMenuClick(Player player, ItemStack item) {
        String itemName = item.getItemMeta().getDisplayName();

        switch (itemName) {
            case "§cMute Players":
                openPlayerSelectorMenu(player, "mute");
                break;

            case "§eView Player Info":
                openPlayerSelectorMenu(player, "info");
                break;

            case "§aTeleport to Players":
                openPlayerSelectorMenu(player, "teleport");
                break;

            case "§fView Player Inventories":
                openPlayerSelectorMenu(player, "inventory");
                break;

            case "§5View Player Ender Chests":
                openPlayerSelectorMenu(player, "enderchest");
                break;

            case "§6Kick Players":
                openPlayerSelectorMenu(player, "kick");
                break;

            case "§4Ban Players":
                openPlayerSelectorMenu(player, "ban");
                break;

            case "§bView Reports":
                MessageUtils.sendMessage(player, "§bReports system - in development");
                break;
        }
    }

    private void openPlayerSelectorMenu(Player player, String action) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8Select Player - " + action.toUpperCase());

        int slot = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (slot >= 54)
                break;

            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            org.bukkit.inventory.meta.SkullMeta meta = (org.bukkit.inventory.meta.SkullMeta) playerHead.getItemMeta();
            meta.setDisplayName("§a" + onlinePlayer.getName());
            meta.setOwningPlayer(onlinePlayer);
            playerHead.setItemMeta(meta);

            menu.setItem(slot, playerHead);
            slot++;
        }

        player.openInventory(menu);
    }

    @EventHandler
    public void onPlayerSelectorClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        // Handle player selector menus
        if (title.startsWith("§8Select Player - ")) {
            event.setCancelled(true);
            String action = title.substring("§8Select Player - ".length());
            String targetPlayerName = clickedItem.getItemMeta().getDisplayName().substring(2); // Remove "§a" prefix
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

            if (targetPlayer == null) {
                MessageUtils.sendMessage(player, "§cPlayer is no longer online!");
                return;
            }

            handlePlayerAction(player, targetPlayer, action);
        }
    }

    private void handlePlayerAction(Player admin, Player target, String action) {
        switch (action) {
            case "ENDERCHEST":
                openEnderChestViewer(admin, target);
                break;
            case "INVENTORY":
                openInventoryViewer(admin, target);
                break;
            case "MUTE_ONLY":
                openMuteDurationMenu(admin, target);
                break;
            case "TELEPORT":
                admin.teleport(target);
                MessageUtils.sendMessage(admin, "§aTeleported to " + target.getName() + "!");
                break;
            case "KICK":
                target.kickPlayer("§cYou have been kicked by " + admin.getName());
                MessageUtils.sendMessage(admin, "§aKicked " + target.getName() + "!");
                break;
            case "BAN":
                target.banPlayer("§cYou have been banned by " + admin.getName());
                MessageUtils.sendMessage(admin, "§aBanned " + target.getName() + "!");
                break;
            case "MUTE":
                openMuteDurationMenu(admin, target);
                break;
            case "INFO":
                showPlayerInfo(admin, target);
                break;
        }
    }

    private void openEnderChestViewer(Player admin, Player target) {
        Inventory enderChest = target.getEnderChest();
        Inventory viewer = Bukkit.createInventory(null, 27, "§8" + target.getName() + "'s Ender Chest");

        for (int i = 0; i < enderChest.getSize(); i++) {
            ItemStack item = enderChest.getItem(i);
            if (item != null) {
                viewer.setItem(i, item.clone());
            }
        }

        admin.openInventory(viewer);
        MessageUtils.sendMessage(admin, "§aViewing " + target.getName() + "'s ender chest!");
    }

    private void openInventoryViewer(Player admin, Player target) {
        Inventory playerInventory = target.getInventory();
        Inventory viewer = Bukkit.createInventory(null, 54, "§8" + target.getName() + "'s Inventory");

        // Copy main inventory (slots 0-35)
        for (int i = 0; i < 36; i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item != null) {
                viewer.setItem(i, item.clone());
            }
        }

        // Copy armor slots (slots 36-39)
        ItemStack helmet = target.getInventory().getHelmet();
        ItemStack chestplate = target.getInventory().getChestplate();
        ItemStack leggings = target.getInventory().getLeggings();
        ItemStack boots = target.getInventory().getBoots();

        if (helmet != null)
            viewer.setItem(36, helmet.clone());
        if (chestplate != null)
            viewer.setItem(37, chestplate.clone());
        if (leggings != null)
            viewer.setItem(38, leggings.clone());
        if (boots != null)
            viewer.setItem(39, boots.clone());

        // Copy offhand (slot 40)
        ItemStack offhand = target.getInventory().getItemInOffHand();
        if (offhand != null) {
            viewer.setItem(40, offhand.clone());
        }

        admin.openInventory(viewer);
        MessageUtils.sendMessage(admin, "§aViewing " + target.getName() + "'s inventory!");
    }

    private void openMuteDurationMenu(Player admin, Player target) {
        Inventory menu = Bukkit.createInventory(null, 27, "§8Mute " + target.getName());

        ItemStack mute5min = createItem(Material.REDSTONE, "§cMute 5 Minutes", "§7Mute for 5 minutes");
        ItemStack mute15min = createItem(Material.REDSTONE, "§cMute 15 Minutes", "§7Mute for 15 minutes");
        ItemStack mute30min = createItem(Material.REDSTONE, "§cMute 30 Minutes", "§7Mute for 30 minutes");
        ItemStack mute1hour = createItem(Material.REDSTONE, "§cMute 1 Hour", "§7Mute for 1 hour");
        ItemStack mutePermanent = createItem(Material.BARRIER, "§cMute Permanent", "§7Mute permanently");

        menu.setItem(10, mute5min);
        menu.setItem(11, mute15min);
        menu.setItem(12, mute30min);
        menu.setItem(13, mute1hour);
        menu.setItem(14, mutePermanent);

        admin.openInventory(menu);
    }

    private void showPlayerInfo(Player admin, Player target) {
        admin.sendMessage("§6=== " + target.getName() + "'s Information ===");
        admin.sendMessage("§eHealth: §a" + Math.round(target.getHealth()) + "/" + Math.round(target.getMaxHealth()));
        admin.sendMessage("§eFood: §a" + target.getFoodLevel() + "/20");
        admin.sendMessage("§eXP Level: §a" + target.getLevel());
        admin.sendMessage("§eGamemode: §a" + target.getGameMode().toString());
        admin.sendMessage("§eWorld: §a" + target.getWorld().getName());
        admin.sendMessage("§eLocation: §a" + Math.round(target.getLocation().getX()) + ", " +
                Math.round(target.getLocation().getY()) + ", " + Math.round(target.getLocation().getZ()));
        admin.sendMessage("§ePing: §a" + target.getPing() + "ms");
        admin.sendMessage("§eIP: §a" + target.getAddress().getAddress().getHostAddress());
    }

    private void handleMuteDurationClick(Player admin, ItemStack clickedItem, String title) {
        String targetName = title.substring("§8Mute ".length());
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            MessageUtils.sendMessage(admin, "§cPlayer is no longer online!");
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();

        switch (itemName) {
            case "§cMute 5 Minutes":
                mutePlayer(admin, target, 5 * 60 * 1000); 
                break;
            case "§cMute 15 Minutes":
                mutePlayer(admin, target, 15 * 60 * 1000); 
                break;
            case "§cMute 30 Minutes":
                mutePlayer(admin, target, 30 * 60 * 1000);
                break;
            case "§cMute 1 Hour":
                mutePlayer(admin, target, 60 * 60 * 1000); 
                break;
            case "§cMute Permanent":
                mutePlayer(admin, target, -1); 
                break;
        }
    }

    private void handleEnderChestMuteClick(Player admin, ItemStack clickedItem, String title) {
        String targetName = title.substring("§8".length(), title.indexOf(" - Ender Chest + Mute"));
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            MessageUtils.sendMessage(admin, "§cPlayer is no longer online!");
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();

        switch (itemName) {
            case "§cMute 5 Minutes":
                mutePlayer(admin, target, 5 * 60 * 1000);
                break;
            case "§cMute 15 Minutes":
                mutePlayer(admin, target, 15 * 60 * 1000);
                break;
            case "§cMute 30 Minutes":
                mutePlayer(admin, target, 30 * 60 * 1000);
                break;
            case "§cMute 1 Hour":
                mutePlayer(admin, target, 60 * 60 * 1000);
                break;
            case "§cMute Permanent":
                mutePlayer(admin, target, -1);
                break;
        }
    }

    private void mutePlayer(Player admin, Player target, long duration) {
        if (duration == -1) {
            target.setMetadata("muted",
                    new org.bukkit.metadata.FixedMetadataValue(org.utils.utils.Main.getInstance(), true));
            MessageUtils.sendMessage(admin, "§aPermanently muted " + target.getName() + "!");
            MessageUtils.sendMessage(target, "§cYou have been permanently muted by " + admin.getName() + "!");
        } else {
            long endTime = System.currentTimeMillis() + duration;
            target.setMetadata("muted_until",
                    new org.bukkit.metadata.FixedMetadataValue(org.utils.utils.Main.getInstance(), endTime));

            String durationText = formatDuration(duration);
            MessageUtils.sendMessage(admin, "§aMuted " + target.getName() + " for " + durationText + "!");
            MessageUtils.sendMessage(target,
                    "§cYou have been muted by " + admin.getName() + " for " + durationText + "!");
        }
    }

    private String formatDuration(long milliseconds) {
        if (milliseconds < 60000) {
            return (milliseconds / 1000) + " seconds";
        } else if (milliseconds < 3600000) {
            return (milliseconds / 60000) + " minutes";
        } else {
            return (milliseconds / 3600000) + " hours";
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("muted")) {
            event.setCancelled(true);
            MessageUtils.sendMessage(player, "§cYou are permanently muted!");
            return;
        }

        if (player.hasMetadata("muted_until")) {
            long muteUntil = player.getMetadata("muted_until").get(0).asLong();
            if (System.currentTimeMillis() < muteUntil) {
                event.setCancelled(true);
                long remaining = muteUntil - System.currentTimeMillis();
                String remainingText = formatDuration(remaining);
                MessageUtils.sendMessage(player, "§cYou are muted for another " + remainingText + "!");
                return;
            } else {
                player.removeMetadata("muted_until", org.utils.utils.Main.getInstance());
            }
        }
    }

    private void handleEnderChestViewerClick(Player admin, InventoryClickEvent event, String title) {
        String targetName = title.substring("§8".length(), title.indexOf("'s Ender Chest"));
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            MessageUtils.sendMessage(admin, "§cPlayer is no longer online!");
            return;
        }

        int clickedSlot = event.getRawSlot();
        if (clickedSlot >= 27)
            return; 

        ItemStack adminHolding = admin.getItemOnCursor();
        if (adminHolding != null && adminHolding.getType() != Material.AIR) {
            ItemStack itemToAdd = adminHolding.clone();
            admin.setItemOnCursor(null);

            target.getEnderChest().setItem(clickedSlot, itemToAdd);

            MessageUtils.sendMessage(admin, "§aAdded " + itemToAdd.getAmount() + "x " +
                    itemToAdd.getType().name().toLowerCase().replace("_", " ") + " to " + target.getName()
                    + "'s ender chest!");
            MessageUtils.sendMessage(target, "§a" + admin.getName() + " added " + itemToAdd.getAmount() + "x " +
                    itemToAdd.getType().name().toLowerCase().replace("_", " ") + " to your ender chest!");

            openEnderChestViewer(admin, target);
            return;
        }

        ItemStack targetItem = target.getEnderChest().getItem(clickedSlot);

        if (targetItem != null) {
            target.getEnderChest().setItem(clickedSlot, null);

            admin.getInventory().addItem(targetItem.clone());

            MessageUtils.sendMessage(admin, "§aTook " + targetItem.getAmount() + "x " +
                    targetItem.getType().name().toLowerCase().replace("_", " ") + " from " + target.getName()
                    + "'s ender chest!");
            MessageUtils.sendMessage(target, "§c" + admin.getName() + " took " + targetItem.getAmount() + "x " +
                    targetItem.getType().name().toLowerCase().replace("_", " ") + " from your ender chest!");

            openEnderChestViewer(admin, target);
        }
    }

    private void handleInventoryViewerClick(Player admin, InventoryClickEvent event, String title) {
        String targetName = title.substring("§8".length(), title.indexOf("'s Inventory"));
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            MessageUtils.sendMessage(admin, "§cPlayer is no longer online!");
            return;
        }

        int clickedSlot = event.getRawSlot();
        if (clickedSlot >= 41)
            return;

        ItemStack adminHolding = admin.getItemOnCursor();
        if (adminHolding != null && adminHolding.getType() != Material.AIR) {
            ItemStack itemToAdd = adminHolding.clone();
            admin.setItemOnCursor(null);

            String itemLocation = "";

            if (clickedSlot < 36) {
                target.getInventory().setItem(clickedSlot, itemToAdd);
                itemLocation = "inventory";
            } else if (clickedSlot == 36) {
                target.getInventory().setHelmet(itemToAdd);
                itemLocation = "helmet";
            } else if (clickedSlot == 37) {
                target.getInventory().setChestplate(itemToAdd);
                itemLocation = "chestplate";
            } else if (clickedSlot == 38) {
                target.getInventory().setLeggings(itemToAdd);
                itemLocation = "leggings";
            } else if (clickedSlot == 39) {
                target.getInventory().setBoots(itemToAdd);
                itemLocation = "boots";
            } else if (clickedSlot == 40) {
                target.getInventory().setItemInOffHand(itemToAdd);
                itemLocation = "offhand";
            }

            String itemName = itemToAdd.getType().name().toLowerCase().replace("_", " ");
            MessageUtils.sendMessage(admin, "§aAdded " + itemToAdd.getAmount() + "x " + itemName + " to " +
                    target.getName() + "'s " + itemLocation + "!");
            MessageUtils.sendMessage(target, "§a" + admin.getName() + " added " + itemToAdd.getAmount() + "x " +
                    itemName + " to your " + itemLocation + "!");

            openInventoryViewer(admin, target);
            return;
        }

        ItemStack targetItem = null;
        String itemLocation = "";

        if (clickedSlot < 36) {
            targetItem = target.getInventory().getItem(clickedSlot);
            itemLocation = "inventory";
        } else if (clickedSlot == 36) {
            targetItem = target.getInventory().getHelmet();
            target.getInventory().setHelmet(null);
            itemLocation = "helmet";
        } else if (clickedSlot == 37) {
            targetItem = target.getInventory().getChestplate();
            target.getInventory().setChestplate(null);
            itemLocation = "chestplate";
        } else if (clickedSlot == 38) {
            targetItem = target.getInventory().getLeggings();
            target.getInventory().setLeggings(null);
            itemLocation = "leggings";
        } else if (clickedSlot == 39) {
            targetItem = target.getInventory().getBoots();
            target.getInventory().setBoots(null);
            itemLocation = "boots";
        } else if (clickedSlot == 40) {
            targetItem = target.getInventory().getItemInOffHand();
            target.getInventory().setItemInOffHand(null);
            itemLocation = "offhand";
        }

        if (targetItem != null) {
            if (clickedSlot < 36) {
                target.getInventory().setItem(clickedSlot, null);
            }

            // Give item to admin
            admin.getInventory().addItem(targetItem.clone());

            String itemName = targetItem.getType().name().toLowerCase().replace("_", " ");
            MessageUtils.sendMessage(admin, "§aTook " + targetItem.getAmount() + "x " + itemName + " from " +
                    target.getName() + "'s " + itemLocation + "!");
            MessageUtils.sendMessage(target, "§c" + admin.getName() + " took " + targetItem.getAmount() + "x " +
                    itemName + " from your " + itemLocation + "!");

            openInventoryViewer(admin, target);
        }
    }

    private void openRoleManagementMenu(Player player, Player target) {
        Inventory menu = Bukkit.createInventory(null, 27, "§8Manage Roles - " + target.getName());

        boolean isMod = PermissionUtils.hasTag(target, "mod");
        ItemStack moderator = createItem(
            Material.EMERALD, 
            "§aToggle Moderator", 
            isMod ? "§cDisable Moderator" : "§aEnable Moderator"
        );

        boolean isAdmin = PermissionUtils.hasTag(target, "admin");
        ItemStack admin = createItem(
            Material.DIAMOND, 
            "§bToggle Admin", 
            isAdmin ? "§cDisable Admin" : "§aEnable Admin"
        );

        boolean isAdminPlus = PermissionUtils.hasTag(target, "admin+");
        ItemStack adminPlus = createItem(
            Material.NETHERITE_INGOT, 
            "§cToggle Admin+", 
            isAdminPlus ? "§cDisable Admin+" : "§aEnable Admin+"
        );

        menu.setItem(11, moderator);
        menu.setItem(13, admin);
        menu.setItem(15, adminPlus);

        player.openInventory(menu);
    }

    private ItemStack createItem(Material material, String name, String loreLine) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList(loreLine));
        item.setItemMeta(meta);
        return item;
    }

}
