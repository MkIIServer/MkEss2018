
package tw.mics.spigot.plugin.mkess2018.listener;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MyListener implements Listener {
	protected JavaPlugin plugin;
	public MyListener(JavaPlugin instance){
		this.plugin = instance;
	    this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}
	
	public void unregisterListener(){
		HandlerList.unregisterAll(this);
	}
}