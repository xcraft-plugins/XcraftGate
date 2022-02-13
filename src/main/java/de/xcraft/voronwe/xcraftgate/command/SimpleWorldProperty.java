package de.xcraft.voronwe.xcraftgate.command;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import org.bukkit.command.CommandSender;

public class SimpleWorldProperty extends WorldSubcommand {
   private final String commandName;
   private final String description;
   private final BiConsumer<DataWorld, Boolean> applyProperty;

   public SimpleWorldProperty(XcraftGate plugin, String commandName, String description, BiConsumer<DataWorld, Boolean> applyProperty) {
      super(plugin);
      this.commandName = commandName;
      this.description = description;
      this.applyProperty = applyProperty;
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld " + this.commandName + " <worldname> <true|false>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         boolean allowed = args.isEmpty() || !((String)args.get(0)).equalsIgnoreCase("false");
         this.applyProperty.accept(this.getWorld(worldName), allowed);
         this.reply(this.description + " on " + worldName + (allowed ? " enabled." : " disabled."));
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? Arrays.asList("true", "false") : Collections.emptyList();
   }
}
