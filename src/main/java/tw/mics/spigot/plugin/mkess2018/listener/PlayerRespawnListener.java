package tw.mics.spigot.plugin.mkess2018.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tw.mics.spigot.plugin.mkess2018.MkEss;
import tw.mics.spigot.plugin.mkess2018.NewbieKits;

import java.util.Random;

public class PlayerRespawnListener extends MyListener {
    static int WORLD_LIMIT = 24000;
    static int PLAYER_DISTANCE_MIN = 300;
    static int PLAYER_DISTANCE_MAX = 800;

    public PlayerRespawnListener(MkEss instance) {
        super(instance);
    }

    static private Location getNewSpawn(Player player) {
        World w = Bukkit.getWorlds().get(0);
        double angle = new Random().nextDouble() * Math.PI * 2;
        double distance = new Random().nextInt(PLAYER_DISTANCE_MAX - PLAYER_DISTANCE_MIN) + PLAYER_DISTANCE_MIN;
        Location l = w.getSpawnLocation().add(Math.cos(angle) * distance, 0, Math.sin(angle) * distance);
        if (l.getX() > WORLD_LIMIT) l.setX(WORLD_LIMIT);
        if (l.getY() > WORLD_LIMIT) l.setY(WORLD_LIMIT);
        if (l.getX() < -WORLD_LIMIT) l.setX(-WORLD_LIMIT);
        if (l.getY() < -WORLD_LIMIT) l.setY(-WORLD_LIMIT);
        Block b = w.getHighestBlockAt(l);
        w.setSpawnLocation(b.getLocation());
        return b.getLocation().add(0.5, 0, 0.5);
    }

    //第一次加入
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFirstJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (!p.hasPlayedBefore()) {
            givePosionEffect(p);
            NewbieKits.giveKits(p);
            p.teleport(getNewSpawn(p));
        }
    }

    //玩家重生
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        givePosionEffect(p);
        if (!event.isBedSpawn()) { // 如果不是床重生
            NewbieKits.giveKits(p);
            event.setRespawnLocation(getNewSpawn(p));
        }
    }

    //終界門回來
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == TeleportCause.END_PORTAL && event.getTo().getWorld().getEnvironment() == Environment.NORMAL) {
            Player p = event.getPlayer();
            if (p.getBedSpawnLocation() == null) { // 如果不是床重生
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.teleport(getNewSpawn(p));
                    }
                });
            }
        }
    }

    private void givePosionEffect(Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2400, 0));
            }
        });
    }
}
