package org.utils.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.utils.utils.utils.MessageUtils;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "player_only");
            return true;
        }
        Player player = (Player) sender;
        var config = JavaPlugin.getProvidingPlugin(getClass()).getConfig();
        String worldName = config.getString("spawn.world", "world");
        double x = config.getDouble("spawn.x", 0.5);
        double y = config.getDouble("spawn.y", 64);
        double z = config.getDouble("spawn.z", 0.5);
        float yaw = (float) config.getDouble("spawn.yaw", 0.0);
        float pitch = (float) config.getDouble("spawn.pitch", 0.0);
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            MessageUtils.sendMessage(player, "&cSpawn world not found in config!");
            return true;
        }
        Location spawnLoc = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawnLoc);
        MessageUtils.sendMessage(player, "&aTeleported to spawn!");
        return true;
    }
}