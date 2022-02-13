package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;

public class SetWorldDifficultyCommand extends WorldSubcommand {
   public SetWorldDifficultyCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setdifficulty <worldname> <peaceful|easy|normal|hard>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         Difficulty newDif = null;
         Difficulty[] var5 = Difficulty.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Difficulty thisDif = var5[var7];
            if (thisDif.toString().equalsIgnoreCase((String)args.get(0))) {
               newDif = thisDif;
            }
         }

         if (newDif == null) {
            this.error("Unknown difficulty.");
            return;
         }

         this.getWorld(worldName).setDifficulty(newDif);
         this.reply("Difficulty on world " + worldName + " set to " + newDif);
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? (List)Stream.of(Difficulty.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList()) : Collections.emptyList();
   }
}
