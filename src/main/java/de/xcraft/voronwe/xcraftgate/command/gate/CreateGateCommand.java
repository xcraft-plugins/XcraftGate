package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.Util;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CreateGateCommand extends GateSubcommand {
   public CreateGateCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate create <gatename>");
      } else if (this.gateExists(gateName)) {
         this.reply("Gate already exists: " + gateName);
      } else {
         Location loc = ((Player)this.sender).getLocation();
         if (this.gateExists(loc)) {
            DataGate var10001 = this.getGateByLocation(loc);
            this.reply("There is already a gate at this location: " + var10001.getName());
         } else {
            DataGate newGate = new DataGate(this.plugin, gateName);
            newGate.setLocation(Util.getSaneLocation(loc));
            this.plugin.getGates().addAndSave(newGate, true);
            this.reply("Gate " + gateName + " created at " + Util.getLocationString(newGate.getLocation()));
         }
      }

   }

   public boolean executableAsConsole() {
      return false;
   }
}
