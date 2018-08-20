package tw.mics.spigot.plugin.mkess2018;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import tw.mics.spigot.plugin.mkess2018.listener.*;

public class MkEss extends JavaPlugin {
    static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        //設定世界
        WorldSetting.runsetting();

        //註冊 listener
        new BedClickListener(this);
        new PlayerRespawnListener(this);
        //new PlayerDeathListener(this);
        new LiquidLimitListener(this);
        new SpeedElytraLimitListener(this);
        new LimitNewbieItemListener(this);
        new ChatLimitListener(this);
        if(isClassExist("me.vagdedes.spartan.api.API")){
            new SpartanFixListener(this);
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    // log system
    public void log(String str, Object... args) {
        String message = String.format(str, args);
        getLogger().info(message);
    }

    private boolean isClassExist(String className) {
        try  {
            Class.forName(className);
            return true;
        }  catch (ClassNotFoundException e) {
            return false;
        }
    }
}
