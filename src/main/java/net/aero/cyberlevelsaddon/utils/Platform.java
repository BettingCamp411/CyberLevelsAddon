package net.aero.cyberlevelsaddon.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;

public final class Platform {

    public static final boolean isFolia;

    private static Method mGetGlobalRegionScheduler;
    private static Method mRunAtFixedRate;
    private static Method mGetPlayerScheduler;
    private static Method mPlayerRun;

    static {
        boolean folia = false;

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;

            Class<?> serverClass = Bukkit.getServer().getClass();
            mGetGlobalRegionScheduler = serverClass.getMethod("getGlobalRegionScheduler");

            Class<?> globalSchedulerClass =
                    Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");

            mRunAtFixedRate = globalSchedulerClass.getMethod(
                    "runAtFixedRate",
                    Plugin.class,
                    java.util.function.Consumer.class,
                    long.class,
                    long.class
            );

            mGetPlayerScheduler = Player.class.getMethod("getScheduler");

            Class<?> playerSchedulerClass =
                    Class.forName("io.papermc.paper.threadedregions.scheduler.EntityScheduler");

            mPlayerRun = playerSchedulerClass.getMethod(
                    "run",
                    Plugin.class,
                    java.util.function.Consumer.class,
                    Object.class
            );

        } catch (Throwable ignored) {
            // Paper/Spigot → no Folia
        }

        isFolia = folia;
    }

    private Platform() {}

    public static Object runRepeatingTask(Plugin plugin, Runnable run, long delay, long period) {
        if (!isFolia) {
            return Bukkit.getScheduler().runTaskTimer(plugin, run, delay, period);
        }

        try {
            Object scheduler = mGetGlobalRegionScheduler.invoke(Bukkit.getServer());

            return mRunAtFixedRate.invoke(
                    scheduler,
                    plugin,
                    (java.util.function.Consumer<Object>) task -> run.run(),
                    delay,
                    period
            );

        } catch (Throwable t) {
            return Bukkit.getScheduler().runTaskTimer(plugin, run, delay, period);
        }
    }

    public static Object runForPlayer(Plugin plugin, Player p, Runnable run) {
        if (!isFolia) {
            return Bukkit.getScheduler().runTask(plugin, run);
        }

        try {
            Object scheduler = mGetPlayerScheduler.invoke(p);

            return mPlayerRun.invoke(
                    scheduler,
                    plugin,
                    (java.util.function.Consumer<Object>) task -> run.run(),
                    null
            );

        } catch (Throwable t) {
            return Bukkit.getScheduler().runTask(plugin, run);
        }
    }

    public static void cancelTask(Object task) {
        if (task == null) return;

        if (task instanceof BukkitTask) {
            if (!((BukkitTask) task).isCancelled()) {
                ((BukkitTask) task).cancel();
            }
            return;
        }

        try {
            Method cancel = task.getClass().getMethod("cancel");
            cancel.invoke(task);
        } catch (Throwable ignored) {}
    }
}
