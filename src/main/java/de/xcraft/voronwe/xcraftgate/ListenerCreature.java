package de.xcraft.voronwe.xcraftgate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ListenerCreature implements Listener {
   private final XcraftGate plugin;

   public ListenerCreature(XcraftGate instance) {
      this.plugin = instance;
   }

   @EventHandler
   public void onCreatureSpawn(CreatureSpawnEvent event) {
      if (this.plugin.getWorlds().get(event.getLocation().getWorld()) != null) {
         this.plugin.getWorlds().get(event.getLocation().getWorld()).checkCreatureLimit();
      }

   }
}
