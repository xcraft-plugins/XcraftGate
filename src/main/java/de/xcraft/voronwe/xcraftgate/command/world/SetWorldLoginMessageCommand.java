package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.Util;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SetWorldLoginMessageCommand extends WorldSubcommand {
   public SetWorldLoginMessageCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setloginmessage <worldname> [message]");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else if (args == null) {
         this.getWorld(worldName).setLoginMessage("none");
         this.reply("Reset login message on world " + worldName + ".");
      } else {
         String newMessage = Util.joinString(args, " ");
         this.getWorld(worldName).setLoginMessage(newMessage);
         this.reply("Login message on world " + worldName + " set to " + newMessage);
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return null;
   }
}
