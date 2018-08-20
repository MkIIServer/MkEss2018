package tw.mics.spigot.plugin.mkess2018.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tw.mics.spigot.plugin.mkess2018.MkEss;

public class ChatLimitListener  extends MyListener{
    public ChatLimitListener(MkEss instance) {
        super(instance);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if(event.getMessage().getBytes().length > 100){
            event.getPlayer().sendMessage(ChatColor.GRAY + "請勿發送過長訊息 (限制 100 字元內)");
            event.setCancelled(true);
        }
    }
}
