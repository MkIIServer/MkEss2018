package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import tw.mics.spigot.plugin.mkess2018.MkEss;

public class PlayerRespawnListener extends MyListener {
    public PlayerRespawnListener(MkEss instance)
    {
        super(instance);
    }
    
    //第一次加入
    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if(!p.hasPlayedBefore()) {
            p.teleport(this.getNewSpawn(p));
        }
    }

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
        
        if(!event.isBedSpawn()){
            event.setRespawnLocation(this.getNewSpawn(p));
        }
	}
	
	@EventHandler
    public void onPortalTeleport(PlayerTeleportEvent event){
        if( event.getCause() == TeleportCause.END_PORTAL && event.getTo().getWorld().getEnvironment() == Environment.NORMAL ){
            Player p = event.getPlayer();
            
            if(p.getBedSpawnLocation() == null){
                p.teleport(this.getNewSpawn(p));
            }
        }
    }
    
    private Location getNewSpawn(Player p) {
        World w = Bukkit.getWorlds().get(0);
        Chunk[] chunks = w.getLoadedChunks();
        Block b = null;
        for(int i=0; i<3; i++){ //最多找 3 次
            Chunk c = chunks[new Random().nextInt(chunks.length)];
            b = w.getHighestBlockAt(c.getBlock(new Random().nextInt(16), 255, new Random().nextInt(16)).getLocation());
            if(!b.isLiquid()) break; //不是水則中斷
        }
        return b.getLocation().add(0.5, 0, 0.5);
	}
}
