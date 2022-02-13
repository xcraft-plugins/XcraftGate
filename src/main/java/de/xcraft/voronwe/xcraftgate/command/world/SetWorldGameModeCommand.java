package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

public class SetWorldGameModeCommand extends WorldSubcommand {
   public SetWorldGameModeCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setgamemode <worldname> <survival|creative|adventure|spectator>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else {
         GameMode newGM = null;
         GameMode[] var5 = GameMode.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            GameMode thisGM = var5[var7];
            if (thisGM.toString().equalsIgnoreCase((String)args.get(0))) {
               newGM = thisGM;
            }
         }

         if (newGM == null) {
            this.error("Unknown gamemode.");
            return;
         }

         this.getWorld(worldName).setGameMode(newGM);
         this.reply("GameMode for world " + worldName + " set to " + newGM);
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      return args.size() < 2 ? (List)Stream.of(GameMode.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList()) : Collections.emptyList();
   }
}
