package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import de.xcraft.voronwe.xcraftgate.generator.GeneratorType;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

public class CreateWorldCommand extends WorldSubcommand {
   public CreateWorldCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld create <worldname> [environment] [seed] [generate structures] [generator settings]");
      } else if (!worldName.matches("^[a-z0-9/._-]+$")) {
         this.error("World name may not contain special characters.");
         this.reply("Usage: /gworld create <worldname> [<environment> [seed]]");
      } else if (this.hasWorld(worldName)) {
         this.reply("World already exists: " + worldName);
      } else {
         String env = args.size() >= 1 ? (String)args.get(0) : "normal";
         Environment worldEnv = null;
         GeneratorType worldGen = null;
         WorldType worldType = WorldType.NORMAL;
         Environment[] var8 = Environment.values();
         int var9 = var8.length;

         int var10;
         for(var10 = 0; var10 < var9; ++var10) {
            Environment thisEnv = var8[var10];
            if (thisEnv.toString().equalsIgnoreCase(env)) {
               worldEnv = thisEnv;
            }
         }

         GeneratorType[] var13 = GeneratorType.values();
         var9 = var13.length;

         for(var10 = 0; var10 < var9; ++var10) {
            GeneratorType thisGen = var13[var10];
            if (thisGen.toString().equalsIgnoreCase(env)) {
               worldGen = thisGen;
               worldEnv = Environment.NORMAL;
            }
         }

         WorldType[] var14 = WorldType.values();
         var9 = var14.length;

         for(var10 = 0; var10 < var9; ++var10) {
            WorldType thisType = var14[var10];
            if (thisType.toString().equalsIgnoreCase(env)) {
               worldType = thisType;
               worldEnv = Environment.NORMAL;
            }
         }

         if (worldEnv == null) {
            this.reply("Unknown environment: " + env);
         } else {
            DataWorld thisWorld = new DataWorld(this.plugin, worldName, worldEnv, worldType, worldGen);
            this.plugin.getWorlds().add(thisWorld);
            if (args.size() < 2) {
               thisWorld.load();
               this.reply("World " + worldName + " created with environment " + env + ".");
            } else {
               long seed;
               try {
                  seed = Long.parseLong((String)args.get(1));
               } catch (NumberFormatException var12) {
                  seed = (long)((String)args.get(1)).hashCode();
               }

               if (args.size() >= 3 && ((String)args.get(2)).equalsIgnoreCase("true")) {
                  thisWorld.setRemoveStructures(true);
               }

               if (args.size() >= 4) {
                  thisWorld.setGeneratorSettings((String)args.get(3));
               }

               thisWorld.load(seed);
               this.reply("World " + worldName + " created with environment " + env + ".");
            }
         }
      }
   }

   public List<String> tabComplete(List<String> args) {
      if (args.size() <= 1) {
         return null;
      } else if (args.size() <= 2) {
         Stream<? extends Enum<?>> generatorStream = Stream.of(GeneratorType.values()).filter((env) -> {
            return env != GeneratorType.DEFAULT;
         });
         Stream<? extends Enum<?>> worldTypeStream = Stream.of(WorldType.values()).filter((env) -> {
            return env != WorldType.NORMAL;
         });
         Stream<? extends Enum<?>> environments = Stream.of(Environment.values());
         return (List)Stream.of(environments, generatorStream, worldTypeStream).flatMap(Function.identity()).map(Object::toString).map(String::toLowerCase).collect(Collectors.toList());
      } else {
         return Collections.emptyList();
      }
   }
}
