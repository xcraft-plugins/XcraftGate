package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.Arrays;
import java.util.List;

public class SetGateDenySilentCommand extends GateSubcommand {
   public SetGateDenySilentCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate setdenysilent <gatename> <true|false>");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         boolean denysilent = args.isEmpty() || ((String)args.get(0)).equalsIgnoreCase("true");
         this.getGate(gateName).setDenySilent(denysilent);
         this.reply("Gate " + gateName + " denys usage " + (denysilent ? "silently." : "loudly."));
      }

   }

   public List<String> tabComplete(List<String> args) {
      return args.size() == 2 ? Arrays.asList("true", "false") : this.findGates(args, 1);
   }
}
