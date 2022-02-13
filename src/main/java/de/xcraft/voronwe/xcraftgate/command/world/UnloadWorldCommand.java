package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;

public class UnloadWorldCommand extends WorldSubcommand {
   public UnloadWorldCommand(XcraftGate instance) {
      super(instance);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld unload <worldname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("Unknown world: " + worldName);
      } else if (!this.getWorld(worldName).isLoaded()) {
         this.reply("World " + worldName + " is not loaded.");
      } else if (this.plugin.getServer().getWorld(worldName).getPlayers().size() > 0) {
         this.error("Unable to unload world with active players.");
      } else {
         this.getWorld(worldName).unload();
         this.reply("Unloaded world " + worldName);
      }

   }

   public List<String> tabComplete(List<String> args) {
      if (args.size() < 2) {
         Collection<String> names = this.plugin.getWorlds().loaded();
         if (args.size() == 0) {
            return new ArrayList(names);
         } else {
            List<String> result = (List)names.stream().filter((name) -> {
               return name.toLowerCase().contains(((String)args.get(0)).toLowerCase());
            }).collect(Collectors.toList());
            return (List)(result.isEmpty() ? new ArrayList(names) : result);
         }
      } else {
         return Collections.emptyList();
      }
   }
}
