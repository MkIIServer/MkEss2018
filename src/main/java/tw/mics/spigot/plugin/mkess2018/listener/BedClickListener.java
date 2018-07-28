package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import tw.mics.spigot.plugin.mkess2018.MkEss;

public class BedClickListener extends MyListener {
	public BedClickListener(MkEss instance)
	{
	    super(instance);
	}
	
    private final static Material[] beds = {
        Material.BLACK_BED,
        Material.BLUE_BED,
        Material.BROWN_BED,
        Material.CYAN_BED,
        Material.GRAY_BED,
        Material.GREEN_BED,
        Material.LIGHT_BLUE_BED,
        Material.LIGHT_GRAY_BED,
        Material.LIME_BED,
        Material.MAGENTA_BED,
        Material.ORANGE_BED,
        Material.PINK_BED,
        Material.PURPLE_BED,
        Material.RED_BED,
        Material.WHITE_BED,
        Material.YELLOW_BED,
    };

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.isCancelled())return;
        Block b = event.getClickedBlock();
        Player p = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(
                Arrays.asList(beds).contains(b.getType()) &&
                b.getWorld().getEnvironment() == Environment.NORMAL &&
                !this.checkPlayerSpawn(b, p)
            ){
                p.setBedSpawnLocation(b.getLocation());
                p.sendMessage("§a重生點已紀錄, 記得床旁要有空位且地板為實心不透明.");
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBedRemoved(BlockBreakEvent event){
        Block b = event.getBlock();
        Player p = event.getPlayer();
        if(
            Arrays.asList(beds).contains(b.getType()) &&
            this.checkPlayerSpawn(b, p)
        ){
            p.setBedSpawnLocation(null);
            p.sendMessage("§c請注意, 您已拆除您重生點的床, 將無法重生於此.");
        }
    }

    //確認玩家重生安全
	private boolean checkPlayerSpawn(Block b, Player p) {
        if(b == null || p.getBedSpawnLocation() == null) return false;
        if(b.getWorld() != p.getBedSpawnLocation().getWorld()) return false;
        Double dist = b.getLocation().distance(p.getBedSpawnLocation());
        if(dist <= 2.24)
            return true;
        return false;
	}
}