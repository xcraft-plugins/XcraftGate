package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

public class SetWorldTimeCommand extends WorldSubcommand {
   public SetWorldTimeCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld settime <worldname> <sunrise|noon|sunset|midnight>");
      } else if (args.size() == 0) {
         this.error("No time given.");
         this.reply("Usage: /gworld settime <worldname> <sunrise|noon|sunset|midnight>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         DataWorld.DayTime[] var4 = DataWorld.DayTime.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            DataWorld.DayTime thisTime = var4[var6];
            if (thisTime.toString().equalsIgnoreCase((String)args.get(0))) {
               this.getWorld(worldName).setDayTime(thisTime);
               this.reply("Time of world " + worldName + " changed to " + (String)args.get(0) + ".");
               return;
            }
         }

         this.reply("Unknown time: " + (String)args.get(0) + ". Use \"sunrise\", \"noon\", \"sunset\" or \"midnight\"");
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? (List)Stream.of(DataWorld.DayTime.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList()) : Collections.emptyList();
   }
}
