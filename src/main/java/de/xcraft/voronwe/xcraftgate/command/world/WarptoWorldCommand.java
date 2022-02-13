package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarptoWorldCommand extends WorldSubcommand {
   public WarptoWorldCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld warpto <worldname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         if (!this.getWorld(worldName).isLoaded()) {
            this.getWorld(worldName).load();
         }

         Location loc = this.getWorld(worldName).getWorld().getSpawnLocation();
         if (loc != null) {
            ((Player)sender).teleport(loc);
         } else {
            this.error("Couldn't find a safe spot at your destination");
         }
      }

   }

   public boolean executableAsConsole() {
      return false;
   }
}
