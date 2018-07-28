package tw.mics.spigot.plugin.mkess2018.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tw.mics.spigot.plugin.mkess2018.MkEss;

public class PlayerRespawnListener extends MyListener {
    static int RANDOM_SPAWN_MAX = 1000;
    static int PLAYER_DISTANCE_MIN = 500;
    static int PLAYER_DISTANCE_MAX = 1000;

    public PlayerRespawnListener(MkEss instance)
    {
        super(instance);
    }

    //新手裝備不會噴
    /* 因為消失詛咒有用所以暫時移除
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        List<ItemStack> drops = event.getDrops();
        Iterator<ItemStack> itr = drops.iterator();
        while(itr.hasNext()){
            ItemStack i = itr.next();
            if(i.getItemMeta().getLore().contains("新手裝備")){
                itr.remove();
            }
        }
    }
    */
    
    //第一次加入
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFirstJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if(!p.hasPlayedBefore()) {
            givePosionEffect(p);
            giveKits(p);
            p.teleport(getNewSpawn(p));
        }
    }

	//玩家重生
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
        givePosionEffect(p);
        if(!event.isBedSpawn()){ // 如果不是床重生
            giveKits(p);
            event.setRespawnLocation(getNewSpawn(p));
        }
	}
    
    //終界門回來
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalTeleport(PlayerTeleportEvent event){
        if( event.getCause() == TeleportCause.END_PORTAL && event.getTo().getWorld().getEnvironment() == Environment.NORMAL ){
            Player p = event.getPlayer();
            if(p.getBedSpawnLocation() == null){ // 如果不是床重生
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                    @Override
                    public void run() {
                        p.teleport(getNewSpawn(p));
                    }
                });
            }
        }
    }
    
    static private Location getNewSpawn(Player player) {
        World w = Bukkit.getWorlds().get(0);
        List<Player> players = new LinkedList<Player>(w.getPlayers());
        Iterator<Player> itr = players.iterator();
        while(itr.hasNext()){
            Player p = itr.next();
            if(
                p.getUniqueId() == player.getUniqueId() ||
                p.getGameMode() != GameMode.SURVIVAL
            ){
                itr.remove();
                continue;
            }
        }
        Block b = null;
        if(players.size() < 5){ //如果現界玩家數 < 5 則隨機重生
            b = w.getHighestBlockAt(
                new Random().nextInt(RANDOM_SPAWN_MAX * 2) - RANDOM_SPAWN_MAX, 
                new Random().nextInt(RANDOM_SPAWN_MAX * 2) - RANDOM_SPAWN_MAX
            );
        } else { //其他則挑一個玩家距離 200 重生
            Player target_p = (Player) players.get(new Random().nextInt(players.size()));
            double angle = new Random().nextDouble() * Math.PI * 2;
            double distance = new Random().nextInt(PLAYER_DISTANCE_MAX - PLAYER_DISTANCE_MIN) + PLAYER_DISTANCE_MIN;
            b = w.getHighestBlockAt(target_p.getLocation().add(Math.cos(angle) * distance, 0, Math.sin(angle) * distance));
        }
        return b.getLocation().add(0.5, 0, 0.5);
    }
    
    private final static Color NEWBIE_COLOR = Color.fromRGB(204, 255, 0);
    private final static String[] NEWBIE_STRING = {
        "新手裝備"
    };

    private void giveKits(Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run() {
                p.sendMessage(ChatColor.GREEN + "您已隨機重生, 新手裝備已發送. (附有簡易教學)");

                List<String> strs;
                ItemStack cookie = new ItemStack(Material.COOKIE, 64);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "新手裝備上都會有一些教學.");
                strs.add(ChatColor.GREEN + "新手裝備隨機重生時都會贈送.");
                strs.add(ChatColor.GREEN + "不建議在公開頻道宣揚自己的座標.");
                strs.add(ChatColor.GREEN + "詳細遊戲設定可以使用指令 /help 查詢.");
                setNewbieItemMeta(cookie, strs);

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
                strs.add(ChatColor.GREEN + "對床右鍵可以記錄重生點.");
                strs.add(ChatColor.GREEN + "重生於床時不會贈送新手裝備.");
                setNewbieItemMeta(bed, strs);

                ItemStack axe = new ItemStack(Material.STONE_AXE, 1);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "死亡身上全部的東西都會噴出來!");
                strs.add(ChatColor.GREEN + "地上物品消失時間為 2 分鐘, 請特別注意!");
                setNewbieItemMeta(axe, strs);

                ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
                strs = new ArrayList<String>();
                strs.add("");
                strs.add(ChatColor.GREEN + "不要把所有的資源放在同一個地方!");
                strs.add(ChatColor.GREEN + "終界箱是這個世界上最安全的地方!");
                setNewbieItemMeta(pickaxe, strs);

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
                p.getInventory().addItem(boat);
                p.getInventory().addItem(bed);
            }
        });
    }

    private void setNewbieItemMeta(ItemStack i){
        setNewbieItemMeta(i, null);
    }

    private void setNewbieItemMeta(ItemStack i, List<String> strs){
        ItemMeta meta = i.getItemMeta();
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        if(meta instanceof LeatherArmorMeta){
            ((LeatherArmorMeta)meta).setColor(NEWBIE_COLOR);
        }
        LinkedList<String> lores = new LinkedList<String>(Arrays.asList(NEWBIE_STRING));
        if(strs != null) lores.addAll(strs);
        meta.setLore(lores);
        i.setItemMeta(meta);
    }

	private void givePosionEffect(Player p) {        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run() {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2400, 0));
            }
        });
	}
}
