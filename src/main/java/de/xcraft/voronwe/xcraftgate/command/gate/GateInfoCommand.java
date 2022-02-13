package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;

public class GateInfoCommand extends GateSubcommand {
   public GateInfoCommand(XcraftGate instance) {
      super(instance);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate info <gatename>");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         this.reply("Info for gate " + gateName);
         this.getGate(gateName).sendInfo(this.sender);
      }

   }

   public List<String> tabComplete(List<String> args) {
      return this.findGates(args, 1);
   }
}
