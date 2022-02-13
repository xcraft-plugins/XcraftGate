package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListGatesCommand extends GateSubcommand {
   public ListGatesCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      List<String> gateNames = new ArrayList(this.plugin.getGates().names());
      gateNames.sort(String::compareToIgnoreCase);
      this.reply(String.join(", ", gateNames));
   }

   public List<String> tabComplete(List<String> args) {
      return Collections.emptyList();
   }
}
