package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListWorldsCommand extends WorldSubcommand {
   public ListWorldsCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      StringBuilder worlds = new StringBuilder();
      Iterator var5 = this.plugin.getWorlds().iterator();

      while(var5.hasNext()) {
         DataWorld thisWorld = (DataWorld)var5.next();
         worlds.append(", ").append(thisWorld.getName());
         if (thisWorld.isLoaded()) {
            worlds.append("(*)");
         }
      }

      ChatColor var10001 = ChatColor.WHITE;
      this.reply("Worlds: " + var10001 + worlds.substring(2));
      this.reply("World marked with (*) are currently active on the server.");
   }

   public List<String> tabComplete(List<String> args) {
      return Collections.emptyList();
   }
}
