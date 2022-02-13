package de.xcraft.voronwe.xcraftgate;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class ListenerWorld implements Listener {
   private final XcraftGate plugin;

   public ListenerWorld(XcraftGate instance) {
      this.plugin = instance;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onWorldLoad(WorldLoadEvent event) {
      World world = event.getWorld();
      this.plugin.getWorlds().onWorldLoad(world);
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onWorldUnload(WorldUnloadEvent event) {
      World world = event.getWorld();
      this.plugin.logInfo("trying to unload world " + world.getName());
   }
}
