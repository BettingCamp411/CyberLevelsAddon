package net.aero.cyberlevelsaddon.listeners;

import net.aero.cyberlevelsaddon.CyberLevelsAddon;
import net.aero.cyberlevelsaddon.addons.ExpBarAddon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final ExpBarAddon expBarAddon = CyberLevelsAddon.inst.getExpBarAddon();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        expBarAddon.getPlayerCache().remove(p.getUniqueId());
    }
}
