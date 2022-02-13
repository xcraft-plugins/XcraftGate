package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWorldSpawnCommand extends WorldSubcommand {
   public SetWorldSpawnCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      Location loc = ((Player)sender).getLocation();
      loc.getWorld().setSpawnLocation((int)Math.floor(loc.getX()), (int)Math.floor(loc.getY()), (int)Math.floor(loc.getZ()));
      this.reply("Set spawn location of " + loc.getWorld().getName() + " to your current position.");
   }

   public List<String> tabComplete(List<String> args) {
      return Collections.emptyList();
   }

   public boolean executableAsConsole() {
      return false;
   }
}
