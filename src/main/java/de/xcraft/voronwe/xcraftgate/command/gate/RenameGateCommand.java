package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;

public class RenameGateCommand extends GateSubcommand {
   public RenameGateCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName != null && args.size() != 0) {
         if (!this.gateExists(gateName)) {
            this.reply("Gate not found: " + gateName);
         } else {
            DataGate thisGate = this.getGate(gateName);
            this.plugin.getGates().remove(thisGate);
            thisGate.setName((String)args.get(0));
            this.plugin.getGates().addAndSave(thisGate, true);
            this.reply("Gate " + gateName + " renamed to " + thisGate.getName());
         }
      } else {
         this.error("No gate given.");
         this.reply("Usage: /gate rename <gatename> <new_gatename>");
      }

   }

   public List<String> tabComplete(List<String> args) {
      return args.size() == 2 ? null : this.findGates(args, 1);
   }
}
