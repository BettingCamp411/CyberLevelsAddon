package net.aero.cyberlevelsaddon.utils;

import net.md_5.bungee.api.ChatColor;

public class Color {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String translateHexColorCodes(String input) {
        String result = input;
        for (String part : input.split(" ")) {
            if (part.startsWith("#") && part.length() == 7) {
                try {
                    ChatColor hexColor = ChatColor.of(part);
                    result = result.replace(part, hexColor.toString());
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return result;
    }
}
