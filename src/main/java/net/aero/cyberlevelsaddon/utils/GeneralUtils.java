package net.aero.cyberlevelsaddon.utils;

import net.aero.cyberlevelsaddon.CyberLevelsAddon;
import org.bukkit.Bukkit;

public class GeneralUtils {

    public static void disablePlugin(String message) {
        Bukkit.getLogger().severe(message);
        Bukkit.getServer().getPluginManager().disablePlugin(CyberLevelsAddon.inst);
    }
}
