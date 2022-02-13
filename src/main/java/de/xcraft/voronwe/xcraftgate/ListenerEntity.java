package de.xcraft.voronwe.xcraftgate;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class ListenerEntity implements Listener {
   private final XcraftGate plugin;

   public ListenerEntity(XcraftGate instance) {
      this.plugin = instance;
   }

   @EventHandler
   public void onEntityRegainHealth(EntityRegainHealthEvent event) {
      if (this.plugin.getWorlds().get(event.getEntity().getWorld()).isSuppressHealthRegain() && (event.getRegainReason() == RegainReason.REGEN || event.getRegainReason() == RegainReason.SATIATED)) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onFoodLevelChange(FoodLevelChangeEvent event) {
      if (this.plugin.getWorlds().get(event.getEntity().getWorld()).isSuppressHunger()) {
         HumanEntity var3 = event.getEntity();
         if (var3 instanceof Player) {
            Player player = (Player)var3;
            event.setFoodLevel(20);
            player.setSaturation(20.0F);
            player.setExhaustion(0.0F);
         }
      }

   }
}
