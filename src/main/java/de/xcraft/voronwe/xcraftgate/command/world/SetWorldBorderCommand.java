package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SetWorldBorderCommand extends WorldSubcommand {
   public SetWorldBorderCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setborder <worldname> <#border>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         int border;
         try {
            border = Integer.parseInt((String)args.get(0));
         } catch (Exception var6) {
            String var10001 = args.size() > 0 ? (String)args.get(0) : "<null>";
            this.reply("Invalid number: " + var10001);
            this.reply("Usage: /gworld setborder <worldname> <#border>");
            return;
         }

         if (border <= 0) {
            this.getWorld(worldName).setBorder(0);
            this.reply("Border of world " + worldName + " removed.");
         } else {
            this.getWorld(worldName).setBorder(border);
            this.reply("Border of world " + worldName + " set to " + border + ".");
         }
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? Arrays.asList("0", "1000", "3000", "6000") : Collections.emptyList();
   }
}
