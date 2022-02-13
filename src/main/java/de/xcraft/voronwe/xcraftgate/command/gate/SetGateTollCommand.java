package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;

public class SetGateTollCommand extends GateSubcommand {
   public SetGateTollCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate settoll <gatename> <amount>");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         if (this.plugin.getPluginManager().getEconomy() == null) {
            this.sender.sendMessage(ChatColor.RED + "ERROR: No economy plugin was found, so this setting has no effect.");
            return;
         }

         double toll;
         try {
            toll = Double.parseDouble((String)args.get(0));
         } catch (Exception var6) {
            this.sender.sendMessage(ChatColor.RED + "ERROR: invalid currency amount");
            return;
         }

         this.getGate(gateName).setToll(toll);
         this.reply("Gate " + gateName + (toll > 0.0D ? " now collecting " + this.plugin.getPluginManager().getEconomy().format(toll) + " toll." : " doesn't collect tolls"));
      }

   }

   public List<String> tabComplete(List<String> args) {
      return args.size() == 2 ? Arrays.asList("0", "100", "300") : this.findGates(args, 1);
   }
}
