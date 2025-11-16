package net.aero.cyberlevelsaddon.addons;

import com.bitaspire.cyberlevels.CyberLevels;
import com.bitaspire.cyberlevels.user.LevelUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.aero.cyberlevelsaddon.CyberLevelsAddon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static net.aero.cyberlevelsaddon.utils.Color.translateHexColorCodes;

public class LevelColorAddon extends PlaceholderExpansion {

    private final CyberLevelsAddon plugin = CyberLevelsAddon.inst;
    private final CyberLevels cyInst = CyberLevels.instance();

    @Override
    public @NotNull String getIdentifier() {
        return "cla";
    }

    @Override
    public @NotNull String getAuthor() {
        return "UnsortedHero_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.5.2";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        LevelUser<?> user = cyInst.userManager().getUser(player);
        if (user == null) return "0";

        int currentLevel = (int) user.getLevel();
        String colorToUse = getColorForLevel(currentLevel);
        String symbolToUse = getSymbolForLevel(currentLevel);

        switch (params.toLowerCase()) {
            case "cla_level_color":
            case "level_color":
                return formatString("placeholder.cla_level_color", "&7[{level}{symbol}&7]", colorToUse, symbolToUse, currentLevel);
            case "cla_color_level":
            case "color_level":
                return formatString("placeholder.cla_color_level", "{level}", colorToUse, symbolToUse, currentLevel);
            case "cla_color_symbol":
            case "color_symbol":
                return formatString("placeholder.cla_color_symbol", "{symbol}", colorToUse, symbolToUse, currentLevel);
            default:
                return null;
        }
    }

    private String formatString(String path, String defaultFormat, String color, String symbol, int level) {
        String formatted = plugin.getConfig().getString(path, defaultFormat)
                .replace("{level}", color + level)
                .replace("{symbol}", symbol);
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(formatted));
    }

    private String getColorForLevel(int level) {
        String defaultColor = plugin.getConfig().getString("default-color", "&7");

        List<Map<?, ?>> levelColors = plugin.getConfig().getMapList("level-colors");

        String colorToUse = defaultColor;
        for (Map<?, ?> entry : levelColors) {
            Object levelObj = entry.get("level");
            Object colorObj = entry.get("color");

            if (levelObj instanceof Integer && colorObj instanceof String) {
                int threshold = (Integer) levelObj;
                String color = (String) colorObj;

                if (level <= threshold) {
                    colorToUse = color;
                    break;
                }
            }
        }

        return colorToUse;
    }

    private String getSymbolForLevel(int level) {
        String defaultSymbol = plugin.getConfig().getString("default-symbol", "∆");

        List<Map<?, ?>> levelSymbols = plugin.getConfig().getMapList("level-symbols");

        String symbolToUse = defaultSymbol;
        for (Map<?, ?> entry : levelSymbols) {
            Object levelObj = entry.get("level");
            Object symbolObj = entry.get("symbol");

            if (levelObj instanceof Integer && symbolObj instanceof String) {
                int threshold = (Integer) levelObj;
                String symbol = (String) symbolObj;

                if (level <= threshold) {
                    symbolToUse = symbol;
                    break;
                }
            }
        }

        return symbolToUse;
    }
}
