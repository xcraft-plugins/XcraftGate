package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpGateCommand extends GateSubcommand {
   public WarpGateCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      if (gateName == null) {
         this.error("No gate given.");
         this.reply("Usage: /gate warp <gatename> [playername]");
      } else if (!this.gateExists(gateName)) {
         this.reply("Gate not found: " + gateName);
      } else {
         Player player;
         if (args.size() == 0) {
            CommandSender var4 = this.sender;
            if (var4 instanceof Player) {
               player = (Player)var4;
               this.plugin.justTeleportedFrom.put(player.getUniqueId(), this.getGate(gateName).getLocation());
               this.getGate(gateName).portHere(player);
            } else {
               this.error("Cannot warp self from console");
            }
         } else if (args.size() == 1) {
            player = this.plugin.getServer().getPlayer((String)args.get(0));
            if (player == null) {
               this.error("Player \"" + (String)args.get(0) + "\" not found.");
               this.reply("Usage: /gate warp <gatename> [playername]");
               return;
            }

            this.plugin.justTeleportedFrom.put(player.getUniqueId(), this.getGate(gateName).getLocation());
            this.getGate(gateName).portHere(player);
         } else {
            this.error("Too many arguments.");
            this.reply("Usage: /gate warp <gatename> [playername]");
         }
      }

   }

   public List<String> tabComplete(List<String> args) {
      return args.size() < 2 ? this.findGates(args, 1) : this.findPlayers(args, 2);
   }

   public boolean executableAsConsole() {
      return true;
   }
}
