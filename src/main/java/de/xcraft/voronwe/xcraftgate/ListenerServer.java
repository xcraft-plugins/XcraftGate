package de.xcraft.voronwe.xcraftgate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class ListenerServer implements Listener {
   private final XcraftGate plugin;

   public ListenerServer(XcraftGate instance) {
      this.plugin = instance;
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPluginDisable(PluginDisableEvent event) {
      this.plugin.getPluginManager().checkDisabledPlugin(event.getPlugin());
   }
}
