package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tw.mics.spigot.plugin.mkess2018.MkEss;

public class PlayerRespawnListener extends MyListener {
    static int RANDOM_SPAWN_MAX = 1000;
    static int PLAYER_DISTANCE_MIN = 300;
    static int PLAYER_DISTANCE_MAX = 500;

    public PlayerRespawnListener(MkEss instance)
    {
        super(instance);
    }
    
    //第一次加入
    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if(!p.hasPlayedBefore()) {
            givePosionEffect(p);
            giveKits(p);
            p.teleport(getNewSpawn(p));
        }
    }

	//玩家重生
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
        givePosionEffect(p);
        if(!event.isBedSpawn()){ // 如果不是床重生
            giveKits(p);
            event.setRespawnLocation(getNewSpawn(p));
        }
	}
    
    //終界門回來
	@EventHandler
    public void onPortalTeleport(PlayerTeleportEvent event){
        if( event.getCause() == TeleportCause.END_PORTAL && event.getTo().getWorld().getEnvironment() == Environment.NORMAL ){
            Player p = event.getPlayer();
            if(p.getBedSpawnLocation() == null){ // 如果不是床重生
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                    @Override
                    public void run() {
                        p.teleport(getNewSpawn(p));
                    }
                });
            }
        }
    }
    
    static private Location getNewSpawn(Player player) {
        World w = Bukkit.getWorlds().get(0);
        List<Player> players = new LinkedList<Player>(w.getPlayers());
        Iterator<Player> itr = players.iterator();
        while(itr.hasNext()){
            Player p = itr.next();
            if(
                p.getUniqueId() == player.getUniqueId() ||
                p.getGameMode() != GameMode.SURVIVAL
            ){
                itr.remove();
                continue;
            }
        }
        Block b = null;
        if(players.size() < 5){ //如果現界玩家數 < 5 則隨機重生
            b = w.getHighestBlockAt(
                new Random().nextInt(RANDOM_SPAWN_MAX * 2) - RANDOM_SPAWN_MAX, 
                new Random().nextInt(RANDOM_SPAWN_MAX * 2) - RANDOM_SPAWN_MAX
            );
        } else { //其他則挑一個玩家距離 200 重生
            Player target_p = (Player) players.get(new Random().nextInt(players.size()));
            double angle = new Random().nextDouble() * Math.PI * 2;
            double distance = new Random().nextInt(PLAYER_DISTANCE_MAX - PLAYER_DISTANCE_MIN) + PLAYER_DISTANCE_MIN;
            b = w.getHighestBlockAt(target_p.getLocation().add(Math.cos(angle) * distance, 0, Math.sin(angle) * distance));
        }
        return b.getLocation().add(0.5, 0, 0.5);
	}

    private void giveKits(Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run() {
                ItemStack cookie = new ItemStack(Material.COOKIE, 64);
                ItemStack boat = new ItemStack(Material.BOAT, 1);
                ItemStack axe = new ItemStack(Material.WOOD_AXE, 1);
                ItemStack pickaxe = new ItemStack(Material.WOOD_PICKAXE, 1);
                p.getInventory().addItem(cookie);
                p.getInventory().addItem(boat);
                p.getInventory().addItem(axe);
                p.getInventory().addItem(pickaxe);
            }
        });
	}

	private void givePosionEffect(Player p) {        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run() {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2400, 0));
            }
        });
	}
}
