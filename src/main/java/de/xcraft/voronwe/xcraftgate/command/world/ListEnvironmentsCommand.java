package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import de.xcraft.voronwe.xcraftgate.generator.GeneratorType;
import java.util.Collections;
import java.util.List;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

public class ListEnvironmentsCommand extends WorldSubcommand {
   public ListEnvironmentsCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      this.reply("Environments provided by Bukkit:");
      Environment[] var4 = Environment.values();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         Environment thisEnv = var4[var6];
         sender.sendMessage(thisEnv.toString());
      }

      WorldType[] var8 = WorldType.values();
      var5 = var8.length;

      for(var6 = 0; var6 < var5; ++var6) {
         WorldType thisEnv = var8[var6];
         if (thisEnv != WorldType.NORMAL) {
            sender.sendMessage(thisEnv.toString());
         }
      }

      this.reply("Environments provided by XcraftGate:");
      GeneratorType[] var9 = GeneratorType.values();
      var5 = var9.length;

      for(var6 = 0; var6 < var5; ++var6) {
         GeneratorType thisEnv = var9[var6];
         if (thisEnv != GeneratorType.DEFAULT) {
            sender.sendMessage(thisEnv.toString());
         }
      }

   }

   public List<String> tabComplete(List<String> args) {
      return Collections.emptyList();
   }
}
