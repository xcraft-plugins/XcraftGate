package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.Collections;
import java.util.List;

public class ReloadGatesCommand extends GateSubcommand {
   public ReloadGatesCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      this.plugin.getGates().reload();
      this.reply("Loaded " + this.plugin.getGates().size() + " gates.");
   }

   public List<String> tabComplete(List<String> args) {
      return Collections.emptyList();
   }
}
