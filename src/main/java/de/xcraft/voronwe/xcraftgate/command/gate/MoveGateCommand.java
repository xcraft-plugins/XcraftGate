package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.Util;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;
import org.bukkit.entity.Player;

public class MoveGateCommand extends GateSubcommand {
   public MoveGateCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate move <gatename>");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         DataGate thisGate = this.getGate(gateName);
         this.plugin.getGates().remove(thisGate);
         Player player = (Player)this.sender;
         thisGate.setLocation(player.getLocation());
         this.plugin.getGates().addAndSave(thisGate, true);
         this.plugin.justTeleported.put(player.getUniqueId(), thisGate.getLocation());
         this.plugin.justTeleportedFrom.put(player.getUniqueId(), thisGate.getLocation());
         this.reply("Gate " + gateName + " moved to " + Util.getLocationString(thisGate.getLocation()));
      }

   }

   public List<String> tabComplete(List<String> args) {
      return this.findGates(args, 1);
   }

   public boolean executableAsConsole() {
      return false;
   }
}
