package tw.mics.spigot.plugin.mkess2018;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tw.mics.spigot.plugin.mkess2018.listener.BedClickListener;
import tw.mics.spigot.plugin.mkess2018.listener.LiquidLimitListener;
import tw.mics.spigot.plugin.mkess2018.listener.PlayerRespawnListener;
import tw.mics.spigot.plugin.mkess2018.listener.SpeedElytraLimitListener;

public class MkEss extends JavaPlugin {
    static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        //註冊 listener
        new BedClickListener(this);
        new PlayerRespawnListener(this);
        new LiquidLimitListener(this);
        new SpeedElytraLimitListener(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
        this.getServer().getScheduler().cancelAllTasks();
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
