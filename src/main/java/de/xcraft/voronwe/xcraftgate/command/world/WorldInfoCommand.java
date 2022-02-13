package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class WorldInfoCommand extends WorldSubcommand {
   public WorldInfoCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld info <worldname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         this.reply("Infos for world " + worldName + ":");
         this.getWorld(worldName).sendInfo(sender);
      }

   }
}
