package de.xcraft.voronwe.xcraftgate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ListenerWeather implements Listener {
   private final XcraftGate plugin;

   public ListenerWeather(XcraftGate instance) {
      this.plugin = instance;
   }

   @EventHandler
   public void onWeatherChange(WeatherChangeEvent event) {
      if (this.plugin.getWorlds().get(event.getWorld()) != null) {
         event.setCancelled(!this.plugin.getWorlds().get(event.getWorld()).isAllowWeatherChange());
      }
   }
}
