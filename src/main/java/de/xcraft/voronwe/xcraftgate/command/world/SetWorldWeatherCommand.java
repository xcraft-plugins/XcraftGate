package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

public class SetWorldWeatherCommand extends WorldSubcommand {
   public SetWorldWeatherCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gworld setweather <worldname> <sun|storm>");
      } else if (args.size() == 0) {
         this.error("No weather given.");
         this.reply("Usage: /gworld setweather <worldname> <sun|storm>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         DataWorld.Weather[] var4 = DataWorld.Weather.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            DataWorld.Weather thisWeather = var4[var6];
            if (thisWeather.toString().equalsIgnoreCase((String)args.get(0))) {
               this.getWorld(worldName).setWeather(thisWeather);
               this.reply("Weather of world " + worldName + " changed to " + (String)args.get(0) + ".");
               return;
            }
         }

         this.reply("Unknown weather type: " + (String)args.get(0) + ". Use \"sun\" or \"storm\"");
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? (List)Stream.of(DataWorld.Weather.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList()) : Collections.emptyList();
   }
}
