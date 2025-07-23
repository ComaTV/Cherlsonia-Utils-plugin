package org.utils.utils.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.utils.utils.utils.MessageUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RtpaCommand implements CommandExecutor {
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "player_only");
            return true;
        }
        Player player = (Player) sender;
        int cooldown = JavaPlugin.getProvidingPlugin(getClass()).getConfig().getInt("rtpa_cooldown", 300);
        long now = System.currentTimeMillis();
        long last = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < cooldown * 1000L) {
            long seconds = (cooldown * 1000L - (now - last)) / 1000L;
            MessageUtils.sendMessage(player, "&cYou must wait " + seconds + " seconds before using /rtpa again.");
            return true;
        }
        World world = player.getWorld();
        int border = 16000;
        double x = (Math.random() - 0.5) * border;
        double z = (Math.random() - 0.5) * border;
        double y = world.getHighestBlockYAt((int) x, (int) z) + 1;
        Location loc = new Location(world, x, y, z);
        player.teleport(loc);
        cooldowns.put(player.getUniqueId(), now);
        MessageUtils.sendMessage(player, "&aYou have been randomly teleported!");
        return true;
    }
}