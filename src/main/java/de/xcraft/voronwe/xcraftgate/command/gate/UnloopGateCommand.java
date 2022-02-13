package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;

public class UnloopGateCommand extends GateSubcommand {
   public UnloopGateCommand(XcraftGate plugin) {
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
            DataGate loop1 = this.getGate(gateName);
            DataGate loop2 = this.getGate(gateTarget);
            if (loop1.getTarget().equals(loop2) && loop2.getTarget().equals(loop1)) {
               loop1.unlink();
               loop2.unlink();
               this.reply("removed gate loop " + gateName + " <=> " + gateTarget);
            } else {
               this.reply("Gates " + gateName + " and " + gateTarget + " aren't linked together");
            }
         }
      } else {
         this.error("No gate(s) given.");
         this.reply("Usage: /gate unloop <gatename> <target_gatename>");
      }

   }

   public List<String> tabComplete(List<String> args) {
      return this.findGates(args, 2);
   }
}
