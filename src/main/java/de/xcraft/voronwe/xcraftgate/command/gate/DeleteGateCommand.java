package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.Iterator;
import java.util.List;

public class DeleteGateCommand extends GateSubcommand {
   public DeleteGateCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate delete <gatename>");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         DataGate thisGate = this.getGate(gateName);
         Iterator var4 = this.plugin.getGates().iterator();

         while(var4.hasNext()) {
            DataGate checkGate = (DataGate)var4.next();
            if (checkGate.hasTarget() && checkGate.getTarget().equals(thisGate)) {
               checkGate.unlink();
            }
         }

         this.plugin.getGates().remove(thisGate);
         this.reply("Gate " + gateName + " removed.");
      }

   }

   public List<String> tabComplete(List<String> args) {
      return this.findGates(args, 1);
   }
}
