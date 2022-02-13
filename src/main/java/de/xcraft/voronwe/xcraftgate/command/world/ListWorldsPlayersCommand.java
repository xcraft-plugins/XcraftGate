package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListWorldsPlayersCommand extends WorldSubcommand {
   public ListWorldsPlayersCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld listplayers <worldname>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         StringBuilder players = new StringBuilder();
         Iterator var5 = this.plugin.getServer().getWorld(worldName).getPlayers().iterator();

         while(var5.hasNext()) {
            Player player = (Player)var5.next();
            players.append(", ").append(player.getName());
         }

         if (players.length() > 0) {
            this.reply("Players in world " + worldName + ": " + players.substring(2));
         } else {
            this.reply("No players in world " + worldName + ".");
         }
      }

   }

   public List<String> tabComplete(List<String> args) {
      if (args.size() < 2) {
         Collection<String> names = this.plugin.getWorlds().loaded();
         if (args.size() == 0) {
            return new ArrayList(names);
         } else {
            List<String> result = (List)names.stream().filter((name) -> {
               return name.toLowerCase().contains(((String)args.get(0)).toLowerCase());
            }).collect(Collectors.toList());
            return (List)(result.isEmpty() ? new ArrayList(names) : result);
         }
      } else {
         return Collections.emptyList();
      }
   }
}
