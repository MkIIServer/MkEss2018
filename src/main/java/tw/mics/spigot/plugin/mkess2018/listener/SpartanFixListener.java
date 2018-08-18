package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums.HackType;
import tw.mics.spigot.plugin.mkess2018.MkEss;

public class SpartanFixListener extends MyListener {
    private final static Material[] SOFT_BLOCK = {
        Material.CLAY,
        Material.FARMLAND,
        Material.GRASS_BLOCK,
        Material.GRASS_PATH,
        Material.GRAVEL,
        Material.MYCELIUM,
        Material.PODZOL,
        Material.COARSE_DIRT,
        Material.DIRT,
        Material.RED_SAND,
        Material.SAND,
        Material.SOUL_SAND,
    };

    private final static Material[] SHOVEL = {
        //Material.DIAMOND_SHOVEL,
        //Material.IRON_SHOVEL,
        //Material.GOLDEN_SHOVEL,
        Material.WOODEN_SHOVEL,
        //Material.STONE_SHOVEL
    };
    
    public SpartanFixListener(MkEss instance) {
        super(instance);
    }

    @EventHandler
    public void onPlayerDamage(BlockDamageEvent event){
        if(!Arrays.asList(SOFT_BLOCK).contains(event.getBlock().getType())) return; //不是指定方塊
        if(event.getItemInHand() == null) return;
        if(
            Arrays.asList(SHOVEL).contains(event.getItemInHand().getType()) &&
            event.getItemInHand().getItemMeta().hasEnchant(Enchantment.DIG_SPEED)
        ){
            API.cancelCheck(event.getPlayer(), HackType.FastBreak, 5);
            API.cancelCheck(event.getPlayer(), HackType.Nuker, 5);
        }
    }
}
