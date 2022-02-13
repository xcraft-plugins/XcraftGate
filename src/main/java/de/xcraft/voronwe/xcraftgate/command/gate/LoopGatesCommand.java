package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;

public class LoopGatesCommand extends GateSubcommand {
   public LoopGatesCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      String gateTarget = args.size() > 0 ? (String)args.get(0) : null;
      if (gateName != null && gateTarget != null) {
         if (!this.gateExists(gateName)) {
            this.reply("Gate not found: " + gateName);
         } else if (!this.gateExists(gateTarget)) {
            this.reply("Gate not found: " + gateTarget);
         } else {
            this.getGate(gateName).linkTo(gateTarget, false);
            this.getGate(gateTarget).linkTo(gateName);
            this.reply("Looped Gates " + gateName + " <=> " + gateTarget);
         }
      } else {
         this.error("No gate(s) given.");
         this.reply("Usage: /gate loop <gatename> <target_gatename>");
      }

   }

   public List<String> tabComplete(List<String> args) {
      return this.findGates(args, 2);
   }
}
