package org.purpurmc.purpurextras.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Optional WorldGuard integration. WorldGuard classes are only touched after checking the plugin is enabled,
 * so this class is safe to reference when WorldGuard is not installed.
 */
public final class WorldGuardHook {

    private WorldGuardHook() {}

    /**
     * Checks if the player is allowed to build at the given location according to WorldGuard regions.
     * Always returns true if WorldGuard is not installed.
     */
    public static boolean canBuild(Player player, Location location) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return true;
        return Checker.canBuild(player, location);
    }

    // Separate class so WorldGuard classes are only loaded when WorldGuard is present
    private static final class Checker {
        private static boolean canBuild(Player player, Location location) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            WorldGuard worldGuard = WorldGuard.getInstance();
            if (worldGuard.getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld())) return true;
            return worldGuard.getPlatform().getRegionContainer().createQuery()
                    .testBuild(BukkitAdapter.adapt(location), localPlayer);
        }
    }
}
