package tw.mics.spigot.plugin.mkess2018.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import tw.mics.spigot.plugin.mkess2018.MkEss;

public class LiquidLimitListener extends MyListener {
    static int LIQUIDLIMIT = 30;
    
    public LiquidLimitListener(MkEss instance) {
        super(instance);
    }

    static int flow_count = 0;
    static BukkitTask count_reset_id;

    // 防止大量液體流動
    @EventHandler
    public void onLiquidFlow(BlockFromToEvent e) {
        if(!e.getBlock().isLiquid()) return;

        if(count_reset_id == null || count_reset_id.isCancelled()){
            count_reset_id = new BukkitRunnable() {
                @Override
                public void run() {
                    LiquidLimitListener.flow_count = 0;
                    MkEss.getPlugin(MkEss.class).log("set 0");
                    cancel();
                }
           }.runTask(MkEss.getInstance());
        }

        MkEss.getPlugin(MkEss.class).log("%d", flow_count);
        if(flow_count++ < LIQUIDLIMIT){
            return;
        }
        MkEss.getPlugin(MkEss.class).log("cancel");
        e.setCancelled(true);
    }
}