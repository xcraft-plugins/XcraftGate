package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;

public class UnlinkGateCommand extends GateSubcommand {
   public UnlinkGateCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate unlink <gatename>");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         this.getGate(gateName).unlink();
         this.reply("removed link from gate " + gateName);
      }

   }

   public List<String> tabComplete(List<String> args) {
      return this.findGates(args, 1);
   }
}
