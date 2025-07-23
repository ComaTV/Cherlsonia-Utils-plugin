package org.utils.utils.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.utils.utils.commands.MuteCommand;
import org.utils.utils.utils.MessageUtils;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        if (MuteCommand.mutedPlayers.contains(name)) {
            event.setCancelled(true);
            MessageUtils.sendMessage(event.getPlayer(), "&cYou are muted and cannot speak in chat.");
        }
    }
}