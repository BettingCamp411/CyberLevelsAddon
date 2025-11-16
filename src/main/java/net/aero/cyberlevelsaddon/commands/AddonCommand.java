package net.aero.cyberlevelsaddon.commands;

import net.aero.cyberlevelsaddon.CyberLevelsAddon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static net.aero.cyberlevelsaddon.utils.Color.colorize;

public class AddonCommand implements CommandExecutor, TabCompleter {

    private final CyberLevelsAddon plugin = CyberLevelsAddon.inst;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("cyberlevelsaddon.reload")) {
            sender.sendMessage(colorize("&cYou do not have permission to use this command."));
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(colorize("&b[CyberLevelsAddon] &cUnknown command. Type '/cyberlevelsaddon reload' to reload the plugin."));
            return true;
        }

        plugin.getServer().getConsoleSender().sendMessage(colorize("&b[CyberLevelsAddon] &fReloading plugin..."));
        plugin.reloadConfig();
        plugin.getServer().getConsoleSender().sendMessage(colorize("&b[CyberLevelsAddon] &fReloaded successfully."));

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("cyberlevelsaddon.reload")) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }
}
