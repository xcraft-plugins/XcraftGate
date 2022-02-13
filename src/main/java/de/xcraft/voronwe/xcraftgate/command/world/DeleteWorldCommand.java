package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class DeleteWorldCommand extends WorldSubcommand {
   public DeleteWorldCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld delete <worldname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         if (!this.getWorld(worldName).unload()) {
            this.error("Unable to delete world with active players.");
            return;
         }

         this.plugin.getWorlds().remove(worldName);
         this.reply("World " + worldName + " removed.");
      }

   }
}
