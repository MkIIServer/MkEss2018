package tw.mics.spigot.plugin.mkess2018;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.util.List;

public class WorldSetting {
    static public void runsetting() {
        List<World> worlds = Bukkit.getServer().getWorlds();
        for (World world : worlds) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 8);
            world.setKeepSpawnInMemory(false);
            //world.setSpawnLocation(world.getHighestBlockAt(0, 0).getLocation());
        }
    }
}