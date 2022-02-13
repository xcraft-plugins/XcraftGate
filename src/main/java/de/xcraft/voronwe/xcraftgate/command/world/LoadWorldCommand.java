package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;

public class LoadWorldCommand extends WorldSubcommand {
   public LoadWorldCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld load <worldname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("Unknown world: " + worldName);
      } else if (this.getWorld(worldName).isLoaded()) {
         this.reply("World " + worldName + " already loaded.");
      } else {
         this.getWorld(worldName).load();
         this.reply("Loaded world " + worldName);
      }

   }

   public List<String> tabComplete(List<String> args) {
      if (args.size() < 2) {
         Collection<String> names = this.plugin.getWorlds().names();
         Collection<String> loaded = this.plugin.getWorlds().loaded();
         if (args.size() == 0) {
            return new ArrayList(names);
         } else {
            List<String> result = (List)names.stream().filter((name) -> {
               return name.toLowerCase().contains(((String)args.get(0)).toLowerCase());
            }).filter((name) -> {
               return !loaded.contains(name);
            }).collect(Collectors.toList());
            return (List)(result.isEmpty() ? new ArrayList(names) : result);
         }
      } else {
         return Collections.emptyList();
      }
   }
}
