package de.xcraft.voronwe.xcraftgate.command;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public abstract class WorldSubcommand extends CommandHelper {
   public WorldSubcommand(XcraftGate plugin) {
      super(plugin);
   }

   public abstract void execute(CommandSender var1, String var2, List<String> var3);

   public boolean hasWorld(World world) {
      return this.hasWorld(world.getName());
   }

   public boolean hasWorld(String world) {
      return this.plugin.getWorlds().get(world) != null;
   }

   public DataWorld getWorld(World world) {
      return this.getWorld(world.getName());
   }

   public DataWorld getWorld(String name) {
      return this.plugin.getWorlds().get(name);
   }

   public List<String> findWorlds(String infix) {
      Collection<String> names = this.plugin.getWorlds().names();
      if (infix == null) {
         return new ArrayList(names);
      } else {
         List<String> result = (List)names.stream().filter((name) -> {
            return name.toLowerCase().contains(infix.toLowerCase());
         }).collect(Collectors.toList());
         return (List)(result.isEmpty() ? new ArrayList(names) : result);
      }
   }

   public List<String> tabComplete(List<String> args) {
      return args.size() < 2 ? this.findWorlds(args.size() == 1 ? (String)args.get(0) : null) : this.tabCompleteFurther(args.subList(1, args.size()));
   }

   public List<String> tabCompleteFurther(List<String> args) {
      return Collections.emptyList();
   }
}
