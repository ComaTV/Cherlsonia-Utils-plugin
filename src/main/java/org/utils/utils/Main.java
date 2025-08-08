package org.utils.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.utils.utils.utils.MessageUtils;
import org.utils.utils.config.ConfigManager;
import org.utils.utils.events.EventManager;
import org.utils.utils.commands.MuteCommand;

public class Main extends JavaPlugin {
    private static Main instance;
    private static ConfigManager staticConfigManager;
    private ConfigManager configManager;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        staticConfigManager = configManager;
        MessageUtils.setConfigManager(configManager);
        eventManager = new EventManager(this);
        eventManager.registerAllListeners();
        MuteCommand.init(this);

        org.utils.utils.utils.CombatManager.loadConfiguration();

        registerCommands();
        getLogger().info("Cherlsonia Utils Plugin enabled!");
    }

    @Override
    public void onDisable() {
        org.utils.utils.utils.CombatManager.clearAllCombat();
        org.utils.utils.utils.CombatIndicator.clearAllBars();
        org.utils.utils.utils.CombatIndicator.clearAllTimers();

        getLogger().info("Cherlsonia Utils Plugin disabled!");
    }

    private void registerCommands() {
        getCommand("spawn").setExecutor(new org.utils.utils.commands.SpawnCommand());
        getCommand("tpa").setExecutor(new org.utils.utils.commands.TpaCommand());
        getCommand("tpaccept").setExecutor(new org.utils.utils.commands.TpacceptCommand());
        getCommand("tpadeny").setExecutor(new org.utils.utils.commands.TpadenyCommand());
        getCommand("help").setExecutor(new org.utils.utils.commands.HelpCommand());
        getCommand("rtpa").setExecutor(new org.utils.utils.commands.RtpaCommand());
        getCommand("feed").setExecutor(new org.utils.utils.commands.FeedCommand());
        getCommand("heal").setExecutor(new org.utils.utils.commands.HealCommand());
        getCommand("clearchat").setExecutor(new org.utils.utils.commands.ClearchatCommand());
        getCommand("rules").setExecutor(new org.utils.utils.commands.RulesCommand());
        getCommand("admintp").setExecutor(new org.utils.utils.commands.AdminTpCommand());
        getCommand("mute").setExecutor(new org.utils.utils.commands.MuteCommand());
        getCommand("unmute").setExecutor(new org.utils.utils.commands.UnmuteCommand());
        getCommand("combat").setExecutor(new org.utils.utils.commands.CombatCommand());
        getCommand("admin").setExecutor(new org.utils.utils.commands.AdminMenuCommand());
        getCommand("mod").setExecutor(new org.utils.utils.commands.ModMenuCommand());
    }

    public static ConfigManager getConfigManager() {
        return staticConfigManager;
    }

    public static Main getInstance() {
        return instance;
    }
}