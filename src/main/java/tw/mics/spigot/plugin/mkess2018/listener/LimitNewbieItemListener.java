package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.GameMode;
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
    private final static InventoryType[] ALLOW_INVENTORY_TYPE = {
        InventoryType.PLAYER,
        InventoryType.CRAFTING
    };

    //死亡掉落
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        List<ItemStack> drops = event.getDrops();
        Iterator<ItemStack> itr = drops.iterator();
        while(itr.hasNext()){
            ItemStack item = itr.next();
            if(isNewbieItem(item)){
                itr.remove();
            }
        }
    }


    //放置於除玩家身上
    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent e) {
        //如果為創造就不判斷
        if(e.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;

        //put in
        if(
                e.getClick() == ClickType.LEFT ||
                e.getClick() == ClickType.RIGHT
        ){
            ItemStack item = e.getCursor();
            if(
                isNewbieItem(item) &&
                e.getClickedInventory() != null &&
                !Arrays.asList(ALLOW_INVENTORY_TYPE).contains(e.getClickedInventory().getType())
            ){
                e.setCancelled(true);
            }
        } else if( //shift + click put in
                e.getClick() == ClickType.SHIFT_LEFT ||
                e.getClick() == ClickType.SHIFT_RIGHT
        ){
            ItemStack item = e.getCurrentItem();
            if(
                isNewbieItem(item) &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.PLAYER &&
                !Arrays.asList(ALLOW_INVENTORY_TYPE).contains(e.getWhoClicked().getOpenInventory().getType())
            ){
                e.setCancelled(true);
            }
        } else if(e.getClick() == ClickType.NUMBER_KEY){ //number key
            ItemStack item = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
            if(
                isNewbieItem(item) &&
                e.getClickedInventory() != null &&
                !Arrays.asList(ALLOW_INVENTORY_TYPE).contains(e.getClickedInventory().getType())
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
            isNewbieItem(item) &&
            e.getInventory() != null &&
            !Arrays.asList(ALLOW_INVENTORY_TYPE).contains(e.getInventory().getType())
        ){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerThrow(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        if(isNewbieItem(item)){
            e.getItemDrop().remove();
        }
    }

    private boolean isNewbieItem(ItemStack item){
        return  item != null &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(LIMIT_LORE_STRING);
    }
}