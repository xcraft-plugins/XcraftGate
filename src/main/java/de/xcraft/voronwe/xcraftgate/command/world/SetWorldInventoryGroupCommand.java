package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SetWorldInventoryGroupCommand extends WorldSubcommand {
   public SetWorldInventoryGroupCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setinventorygroup <worldname> <groupname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else if (args.size() != 1) {
         this.error("Wrong argument count.");
         this.reply("Usage: /gworld setinventorygroup <worldname> <groupname>");
      } else {
         this.getWorld(worldName).setInventoryGroup((String)args.get(0));
         this.reply("Inventory group for world " + worldName + " set to " + (String)args.get(0));
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return (List)(args.size() <= 1 ? new ArrayList(this.plugin.getWorlds().existingInventoryGroups()) : Collections.emptyList());
   }
}
