package tw.mics.spigot.plugin.mkess2018.listener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerDeathListener extends MyListener {
    HashMap<UUID, Date> playerDeath;
    static int BAN_TIME_MIN = 5;

    public PlayerDeathListener(JavaPlugin instance)
    {
        super(instance);
        playerDeath = new HashMap<UUID, Date>();
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(event.getEntity().isOp()) return; //OP Bypass
        Player p = event.getEntity();
        Date date = new Date();
        playerDeath.put(p.getUniqueId(), date);
        p.kickPlayer("您已經死亡, 將暫時無法進入伺服器 " + BAN_TIME_MIN + " 分鐘.");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        Date death_date = playerDeath.get(event.getPlayer().getUniqueId()); 
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -BAN_TIME_MIN );
        if(death_date != null && death_date.after(calendar.getTime())){
            long left = death_date.getTime() - calendar.getTime().getTime();
            event.disallow(Result.KICK_OTHER, "您還在死亡狀態, 還剩下 " + (int) Math.ceil(left/60000.0) + " 分鐘");
        }
    }
}
