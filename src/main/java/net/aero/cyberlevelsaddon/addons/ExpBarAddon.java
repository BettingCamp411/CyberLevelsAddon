package net.aero.cyberlevelsaddon.addons;

import com.bitaspire.cyberlevels.CyberLevels;
import com.bitaspire.cyberlevels.user.LevelUser;
import net.aero.cyberlevelsaddon.CyberLevelsAddon;
import net.aero.cyberlevelsaddon.cache.CachedData;
import net.aero.cyberlevelsaddon.utils.Platform;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ExpBarAddon {

    private final CyberLevelsAddon plugin = CyberLevelsAddon.inst;
    private final FileConfiguration config = plugin.getConfig();
    private final boolean enabled = config.getBoolean("exp-bar-enabled", false);
    private final long updateInterval = config.getLong("exp-bar-update-interval", 20);

    private final Map<UUID, CachedData> playerCache = new ConcurrentHashMap<>();
    private Object task;

    public void run() {
        if (!enabled) return;

        task = Platform.runRepeatingTask(
                plugin,
                () -> Bukkit.getOnlinePlayers().forEach(p ->
                        Platform.runForPlayer(plugin, p, () -> updatePlayer(p))
                ),
                20L,
                updateInterval
        );
    }

    public void stop() {
        Platform.cancelTask(task);
        task = null;
        playerCache.clear();
    }

    public void updatePlayer(Player player) {
        Updater.updateBar(player, playerCache);
    }

    public Map<UUID, CachedData> getPlayerCache() {
        return playerCache;
    }
}

class Updater {

    private static final CyberLevels cyInst = CyberLevels.instance();

    public static void updateBar(Player p, Map<UUID, CachedData> cache) {
        LevelUser<?> user = cyInst.userManager().getUser(p);
        if (user == null) return;

        CachedData cached = cache.computeIfAbsent(p.getUniqueId(), id -> new CachedData());

        int newLevel = (int) user.getLevel();
        double newCurrentExp = user.getExp().doubleValue();
        double newRequiredExp = user.getRequiredExp().doubleValue();

        if (cached.update(newLevel, newCurrentExp, newRequiredExp)) {
            p.setLevel(newLevel);

            float progress = newRequiredExp > 0 ?
                    (float) (newCurrentExp / newRequiredExp) :
                    0f;

            p.setExp(progress);
        }
    }
}
