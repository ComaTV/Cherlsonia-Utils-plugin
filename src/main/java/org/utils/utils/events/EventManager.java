package org.utils.utils.events;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<Listener> listeners = new ArrayList<>();
    private final Plugin plugin;

    public EventManager(Plugin plugin) {
        this.plugin = plugin;
        listeners.add(new ChatListener());
        listeners.add(new CombatListener());
        listeners.add(new MenuListener());
    }

    public void registerAllListeners() {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}