package org.utils.utils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.utils.utils.utils.CombatManager;
import org.utils.utils.utils.MessageUtils;

public class CombatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (attacker.isDead() || victim.isDead()) {
            return;
        }

        CombatManager.startCombat(attacker, victim);

        CombatManager.updateCombat(attacker);
        CombatManager.updateCombat(victim);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        CombatManager.endCombat(player.getUniqueId());

        Player killer = player.getKiller();
        if (killer != null) {
            CombatManager.endCombat(killer.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (CombatManager.isInCombat(player)) {
            Player opponent = CombatManager.getCombatOpponent(player);
            if (opponent != null && opponent.isOnline()) {
                MessageUtils.sendMessage(opponent, "&c[Combat] " + player.getName() + " logged out during combat!");
                MessageUtils.sendMessage(opponent, "&a[Combat] Combat has ended due to opponent logging out.");
            }
        }

        CombatManager.endCombat(player.getUniqueId());

        if (CombatManager.isInCombat(player)) {
            Player opponent = CombatManager.getCombatOpponent(player);
            if (opponent != null && opponent.isOnline()) {
                CombatManager.endCombat(opponent.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (CombatManager.isInCombat(player)) {
            String command = event.getMessage().toLowerCase();

            if (command.startsWith("/spawn") ||
                    command.startsWith("/tpa") ||
                    command.startsWith("/rtpa") ||
                    command.startsWith("/home") ||
                    command.startsWith("/warp") ||
                    command.startsWith("/tp") ||
                    command.startsWith("/teleport")) {

                event.setCancelled(true);
                MessageUtils.sendMessage(player, "&c[Combat] You cannot use teleport commands while in combat!");
                return;
            }

            if (command.startsWith("/kit") ||
                    command.startsWith("/shop") ||
                    command.startsWith("/trade")) {

                event.setCancelled(true);
                MessageUtils.sendMessage(player, "&c[Combat] You cannot use this command while in combat!");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (CombatManager.isInCombat(player)) {
            event.setCancelled(true);
            MessageUtils.sendMessage(player, "&c[Combat] You cannot teleport while in combat!");
        }
    }
}