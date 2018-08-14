package tw.mics.spigot.plugin.mkess2018.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import tw.mics.spigot.plugin.mkess2018.MkEss;

public abstract class MyListener implements Listener {
    protected MkEss plugin;

    public MyListener(MkEss instance) {
        this.plugin = instance;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}