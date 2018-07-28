package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import tw.mics.spigot.plugin.mkess2018.MkEss;

public class LimitNewbieItemListener extends MyListener {
    public LimitNewbieItemListener(MkEss instance) {
        super(instance);
    }

    private final static String LIMIT_LORE_STRING = "新手裝備";

    //死亡掉落
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        List<ItemStack> drops = event.getDrops();
        Iterator<ItemStack> itr = drops.iterator();
        while(itr.hasNext()){
            ItemStack item = itr.next();
            if(
                item != null &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(LIMIT_LORE_STRING)
            ){
                itr.remove();
            }
        }
    }

    //放置於除玩家身上
    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent e) {
        //put in
        if(
                e.getClick() == ClickType.LEFT ||
                e.getClick() == ClickType.RIGHT
        ){
            ItemStack item = e.getCursor();
            if(
                item != null &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(LIMIT_LORE_STRING) &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() != InventoryType.PLAYER
            ){
                e.setCancelled(true);
            }
        }
        
        //shift + click put in
        if(
                e.getClick() == ClickType.SHIFT_LEFT ||
                e.getClick() == ClickType.SHIFT_RIGHT
        ){
            ItemStack item = e.getCurrentItem();
            if(
                item != null &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(LIMIT_LORE_STRING) &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getWhoClicked().getOpenInventory().getType() != InventoryType.PLAYER
            ){
                e.setCancelled(true);
            }
        }
        
        //number key
        if(e.getClick() == ClickType.NUMBER_KEY){
            ItemStack item = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
            if(
                item != null &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(LIMIT_LORE_STRING) &&
                e.getClickedInventory().getType() != InventoryType.PLAYER
            ){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(InventoryDragEvent e){
        //drag in
        ItemStack item = e.getOldCursor();
        if(
                item != null &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(LIMIT_LORE_STRING) &&
                e.getInventory() != null &&
                e.getInventory().getType() != InventoryType.PLAYER
        ){
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerThrow(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        if(
            item != null &&
            item.getItemMeta() != null &&
            item.getItemMeta().getLore() != null &&
            item.getItemMeta().getLore().contains(LIMIT_LORE_STRING) 
        ){
            e.getItemDrop().remove();
        }
    }
}