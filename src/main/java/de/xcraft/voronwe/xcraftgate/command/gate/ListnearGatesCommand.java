package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.Util;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ListnearGatesCommand extends GateSubcommand {
   public ListnearGatesCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      int radius = 10;
      if (gateName != null) {
         try {
            radius = Integer.parseInt(gateName);
         } catch (Exception var16) {
            this.error("Invalid radius number: " + gateName);
            return;
         }
      }

      Location now = ((Player)this.sender).getLocation();
      double xx = now.getX();
      double yy = now.getY();
      double zz = now.getZ();
      boolean fail = true;

      for(int x = -radius; x <= radius; ++x) {
         for(int y = yy - (double)radius > 0.0D ? -radius : (int)(-yy); y <= (y + radius > 127 ? 128 - y : radius); ++y) {
            for(int z = -radius; z <= radius; ++z) {
               DataGate thisGate = this.plugin.getGates().getByLocation(new Location(now.getWorld(), (double)x + xx, (double)y + yy, (double)z + zz));
               if (thisGate != null) {
                  String var10001 = thisGate.getName();
                  this.reply("Found " + var10001 + " at " + Util.getLocationString(thisGate.getLocation()));
                  fail = false;
               }
            }
         }
      }

      if (fail) {
         this.reply("No gates found.");
      }

   }

   public List<String> tabComplete(List<String> args) {
      return args.size() < 2 ? Arrays.asList("1", "2", "4", "8") : Collections.emptyList();
   }

   public boolean executableAsConsole() {
      return false;
   }
}
