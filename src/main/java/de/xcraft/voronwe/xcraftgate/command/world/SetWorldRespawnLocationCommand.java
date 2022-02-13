package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

public class SetWorldRespawnLocationCommand extends WorldSubcommand {
   public SetWorldRespawnLocationCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("Unknown world: " + worldName);
      } else if (args.size() < 1) {
         this.error("No location given.");
         this.reply("Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
      } else {
         String rsLoc = (String)args.get(0);
         DataWorld.RespawnLocation newRSLoc = DataWorld.RespawnLocation.getRespawnLocation(rsLoc);
         if (newRSLoc == null) {
            this.reply("Unknown respawn location: " + rsLoc);
            this.reply("Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
            return;
         }

         if (newRSLoc == DataWorld.RespawnLocation.WORLD) {
            if (args.size() < 2) {
               this.error("No respawn world given.");
               this.reply("Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
               return;
            }

            if (!this.hasWorld((String)args.get(1))) {
               this.reply("Unknown respawn world: " + (String)args.get(1));
               return;
            }

            this.getWorld(worldName).setRespawnWorldName((String)args.get(1));
         }

         this.getWorld(worldName).setRespawnLocation(newRSLoc);
         this.reply("RespawnLocation for world " + worldName + " set to " + newRSLoc + (newRSLoc == DataWorld.RespawnLocation.WORLD ? ": " + (String)args.get(1) : ""));
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? (List)Stream.of(DataWorld.RespawnLocation.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList()) : (args.size() < 3 && ((String)args.get(0)).equalsIgnoreCase("world") ? this.findWorlds((String)args.get(1)) : Collections.emptyList());
   }
}
