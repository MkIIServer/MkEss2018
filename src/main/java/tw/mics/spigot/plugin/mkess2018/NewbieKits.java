package tw.mics.spigot.plugin.mkess2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class NewbieKits {
    
    private final static Color NEWBIE_COLOR = Color.fromRGB(204, 255, 0);
    private final static String[] NEWBIE_STRING = {
        "新手裝備",
        "無法放置於容器",
        "掉落後直接消失"
    };

    public static void giveKits(Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MkEss.instance, new Runnable(){
            @Override
            public void run() {
                p.sendMessage(ChatColor.GREEN + "您已隨機重生, 新手裝備已發送. (附有簡易教學)");

                List<String> strs;
                ItemStack cookie = new ItemStack(Material.COOKIE, 64);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "新手裝備上都會有一些教學.");
                strs.add(ChatColor.GREEN + "隨機重生時都會贈送新手裝備.");
                strs.add(ChatColor.GREEN + "詳細遊戲設定可以使用指令 /help 查詢.");
                setNewbieItemMeta(cookie, strs);

                ItemStack axe = new ItemStack(Material.WOODEN_AXE, 1);
                axe.addEnchantment(Enchantment.DURABILITY, 3);
                axe.addEnchantment(Enchantment.DIG_SPEED, 3);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "死亡身上全部的東西都會噴出來!");
                strs.add(ChatColor.GREEN + "地上物品消失時間為 2 分鐘, 請特別注意!");
                setNewbieItemMeta(axe, strs);

                ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE, 1);
                pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
                pickaxe.addEnchantment(Enchantment.DIG_SPEED, 3);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "不要把所有的資源放在同一個地方!");
                strs.add(ChatColor.GREEN + "終界箱是這個世界上最安全的地方!");
                setNewbieItemMeta(pickaxe, strs);

                ItemStack boat = new ItemStack(Material.OAK_BOAT, 1);
                strs = new ArrayList<String>();
                strs.add("放置後會變為一般物品");
                strs.add("");
                strs.add(ChatColor.GREEN + "重生在海上? 這個船就是給你用的.");
                strs.add(ChatColor.GREEN + "離朋友很遠? 有方法可以上地獄頂端跑圖.");
                setNewbieItemMeta(boat, strs);

                ItemStack bed = new ItemStack(Material.WHITE_BED, 1);
                strs = new ArrayList<String>();
                strs.add("放置後會變為一般物品");
                strs.add("");
                strs.add(ChatColor.GREEN + "放置後對床右鍵可以記錄重生點.");
                strs.add(ChatColor.GREEN + "重生於床時不會贈送新手裝備.");
                setNewbieItemMeta(bed, strs);

                /*
                ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 10);
                strs = new ArrayList<String>();
                strs.add("放置後會變為一般物品");
                strs.add("");
                strs.add(ChatColor.GREEN + "離朋友很遠? 有方法可以上地獄頂端跑圖.");
                strs.add(ChatColor.GREEN + "幹嘛給你黑曜石? 當然就是去地獄囉!");
                setNewbieItemMeta(obsidian, strs);

                ItemStack ender_pearl = new ItemStack(Material.ENDER_PEARL, 16);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "這是上地獄頂端的秘密武器.");
                setNewbieItemMeta(ender_pearl, strs);
                */

                ItemStack leather_helmet = new ItemStack(Material.LEATHER_HELMET, 1);
                leather_helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                setNewbieItemMeta(leather_helmet);

                ItemStack leather_chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                leather_chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                setNewbieItemMeta(leather_chestplate);

                ItemStack leather_leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
                leather_leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                setNewbieItemMeta(leather_leggings);

                ItemStack leather_boots = new ItemStack(Material.LEATHER_BOOTS, 1);
                leather_boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                setNewbieItemMeta(leather_boots);
                
                //身上裝備
                p.getInventory().setHelmet(leather_helmet);
                p.getInventory().setChestplate(leather_chestplate);
                p.getInventory().setLeggings(leather_leggings);
                p.getInventory().setBoots(leather_boots);

                //物資
                p.getInventory().addItem(cookie);
                p.getInventory().addItem(axe);
                p.getInventory().addItem(pickaxe);
                //p.getInventory().addItem(ender_pearl);
                p.getInventory().addItem(boat);
                p.getInventory().addItem(bed);
                //p.getInventory().addItem(obsidian);
            }
        });
    }
    
    private static void setNewbieItemMeta(ItemStack i){
        setNewbieItemMeta(i, null);
    }

    private static void setNewbieItemMeta(ItemStack i, List<String> strs){
        ItemMeta meta = i.getItemMeta();
        if(meta instanceof LeatherArmorMeta){
            ((LeatherArmorMeta)meta).setColor(NEWBIE_COLOR);
        }
        LinkedList<String> lores = new LinkedList<String>(Arrays.asList(NEWBIE_STRING));
        if(strs != null) lores.addAll(strs);
        meta.setLore(lores);
        i.setItemMeta(meta);
    }
}