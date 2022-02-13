package de.xcraft.voronwe.xcraftgate;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
   private final XcraftGate plugin;
   private YamlConfiguration playerInventorys;

   public InventoryManager(XcraftGate plugin) {
      this.plugin = plugin;
   }

   public void save() {
      try {
         this.playerInventorys.save(this.plugin.getConfigFile("playerInventorys.yml"));
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void load() {
      this.playerInventorys = this.plugin.getConfig(this.plugin.getConfigFile("playerInventorys.yml"));
   }

   public void changeInventory(Player player, DataWorld from, DataWorld to) {
      this.saveInventory(player, from, player.getGameMode());
      this.clearInventory(player);
      this.loadInventory(player, to, player.getGameMode());
   }

   public void changeInventroy(Player player, GameMode from, GameMode to, DataWorld world) {
      this.saveInventory(player, world, from);
      this.clearInventory(player);
      this.loadInventory(player, world, to);
   }

   private void saveInventory(Player player, DataWorld world, GameMode mode) {
      if (mode == null) {
         mode = world.getGameMode();
         Logger var10000 = this.plugin.getLogger();
         String var10001 = player.getName();
         var10000.warning(var10001 + " did not have a gamemode. Falling back to " + mode);
      }

      YamlConfiguration var8 = this.playerInventorys;
      UUID var9 = player.getUniqueId();
      ConfigurationSection playerInv = var8.getConfigurationSection(var9 + "." + world.getInventoryGroup() + "." + mode);
      if (playerInv == null) {
         var8 = this.playerInventorys;
         var9 = player.getUniqueId();
         playerInv = var8.createSection(var9 + "." + world.getInventoryGroup() + "." + mode);
      }

      ConfigurationSection inv = playerInv.getConfigurationSection("inventory");
      if (inv == null) {
         inv = playerInv.createSection("inventory");
      }

      playerInv.set("ign", player.getName());
      playerInv.set("health", player.getHealth());
      playerInv.set("food", player.getFoodLevel());
      playerInv.set("exp_total", player.getTotalExperience());
      playerInv.set("exp_level", player.getLevel());
      playerInv.set("exp_tolvl", player.getExp());
      playerInv.set("ver", 1);
      ItemStack[] thisInv = player.getInventory().getContents();

      for(int i = 0; i < thisInv.length; ++i) {
         inv.set(Integer.toString(i), thisInv[i]);
      }

   }

   private void loadInventory(Player player, DataWorld world, GameMode mode) {
      XcraftGate var10000 = this.plugin;
      String var10001 = mode.toString();
      var10000.logInfo("Loading Inventory " + var10001 + " in world " + world.getName() + " (group " + world.getInventoryGroup() + ") for " + player.getUniqueId());
      boolean convertInv = false;
      YamlConfiguration var10 = this.playerInventorys;
      UUID var11 = player.getUniqueId();
      ConfigurationSection playerInv = var10.getConfigurationSection(var11 + "." + world.getInventoryGroup() + "." + mode);
      if (playerInv == null) {
         var10 = this.playerInventorys;
         var11 = player.getUniqueId();
         playerInv = var10.getConfigurationSection(var11 + "." + world.getName() + "." + mode);
         convertInv = true;
         if (playerInv == null) {
            var10 = this.playerInventorys;
            var11 = player.getUniqueId();
            playerInv = var10.createSection(var11 + "." + world.getInventoryGroup() + "." + mode);
            convertInv = false;
         }
      }

      ConfigurationSection inv = playerInv.getConfigurationSection("inventory");
      if (inv == null) {
         inv = playerInv.createSection("inventory");
      }

      int inventorySize = Math.max(player.getInventory().getContents().length, 41);
      ItemStack[] thisInv = new ItemStack[inventorySize];

      int health;
      for(health = 0; health < inventorySize; ++health) {
         thisInv[health] = inv.getItemStack(Integer.toString(health), new ItemStack(Material.AIR));
      }

      player.getInventory().setContents(thisInv);
      if (this.plugin.getConfig().getBoolean("invsep.health")) {
         health = playerInv.getInt("health", 20);
         if (health > 20 | health == 0) {
            health = 20;
         }

         player.setHealth((double)health);
      }

      if (this.plugin.getConfig().getBoolean("invsep.food")) {
         player.setFoodLevel(playerInv.getInt("food", 20));
      }

      if (this.plugin.getConfig().getBoolean("invsep.exp")) {
         player.setTotalExperience(playerInv.getInt("exp_total", 0));
         player.setLevel(playerInv.getInt("exp_level", 0));
         player.setExp((float)playerInv.getDouble("exp_tolvl", 0.0D));
      }

      if (convertInv) {
         var10 = this.playerInventorys;
         var11 = player.getUniqueId();
         var10.set(var11 + "." + world.getName() + "." + mode, (Object)null);
      }

      player.updateInventory();
   }

   private void clearInventory(Player player) {
      player.getInventory().clear();
   }
}
