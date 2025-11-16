package net.aero.cyberlevelsaddon;

import net.aero.cyberlevelsaddon.addons.LevelColorAddon;
import net.aero.cyberlevelsaddon.addons.ExpBarAddon;
import net.aero.cyberlevelsaddon.commands.AddonCommand;
import net.aero.cyberlevelsaddon.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.bitaspire.libs.beanslib.utility.Exceptions.isPluginEnabled;
import static net.aero.cyberlevelsaddon.utils.GeneralUtils.disablePlugin;

public final class CyberLevelsAddon extends JavaPlugin {

    public static CyberLevelsAddon inst;
    private static ExpBarAddon expBarAddon;
    private static LevelColorAddon levelColorAddon;
    private static final Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        inst = this;

        saveDefaultConfig();

        logger.log(Level.INFO,"Checking Dependencies...");
        if (!isPluginEnabled("CyberLevels")) {
            disablePlugin("CyberLevels is not installed. This plugin requires CyberLevels to be installed download it first and restart the server.");
            return;
        }

        if (!isPluginEnabled("PlaceholderAPI")) {
            disablePlugin("PlaceholderAPI is not installed. This plugin requires PlaceholderAPI to be installed download it first and restart the server.");
            return;
        }

        registerAddons();
        registerListener();
        registerCommands();
    }

    public void registerAddons() {
        logger.log(Level.INFO, "Registering Addons...");

        levelColorAddon = new LevelColorAddon();
        levelColorAddon.register();

        expBarAddon = new ExpBarAddon();
        expBarAddon.run();
    }

    public void registerListener() {
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    public void registerCommands() {
        getCommand("cyberlevelsaddon").setExecutor(new AddonCommand());
        getCommand("cyberlevelsaddon").setTabCompleter(new AddonCommand());
    }

    @Override
    public void onDisable() {
        if (expBarAddon != null) expBarAddon.stop();

        inst = null;
    }

    public LevelColorAddon getLevelColorAddon() {
        return levelColorAddon;
    }

    public ExpBarAddon getExpBarAddon() {
        return expBarAddon;
    }
}
