package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SetWorldCreatureLimitCommand extends WorldSubcommand {
   public SetWorldCreatureLimitCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setcreaturelimit <worldname> <#limit>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         int limit;
         try {
            limit = Integer.parseInt((String)args.get(0));
         } catch (Exception var6) {
            String var10001 = args.size() > 0 ? (String)args.get(0) : "<null>";
            this.reply("Invalid number: " + var10001);
            this.reply("Usage: /gworld setborder <worldname> <#limit>");
            return;
         }

         if (limit <= 0) {
            this.getWorld(worldName).setCreatureLimit(0);
            this.reply("Creature limit of world " + worldName + " removed.");
         } else {
            this.getWorld(worldName).setCreatureLimit(limit);
            this.reply("Creature limit of world " + worldName + " set to " + limit + ".");
         }
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? Arrays.asList("0", "100", "1000") : Collections.emptyList();
   }
}
