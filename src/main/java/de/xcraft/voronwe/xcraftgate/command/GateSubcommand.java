package de.xcraft.voronwe.xcraftgate.command;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;

public abstract class GateSubcommand extends CommandHelper {
   public GateSubcommand(XcraftGate plugin) {
      super(plugin);
   }

   public abstract void execute(String var1, List<String> var2);

   public boolean gateExists(String name) {
      return this.plugin.getGates().has(name);
   }

   public boolean gateExists(Location location) {
      return this.plugin.getGates().getByLocation(location) != null;
   }

   public DataGate getGate(String name) {
      return this.plugin.getGates().get(name);
   }

   public DataGate getGateByLocation(Location loc) {
      return this.plugin.getGates().getByLocation(loc);
   }

   public List<String> findGates(List<String> args, int limit) {
      Predicate<? super String> duplicates = (name) -> {
         return args.isEmpty() || !((String)args.get(0)).equalsIgnoreCase(name);
      };
      if (args.size() > limit) {
         return Collections.emptyList();
      } else {
         if (args.size() > 0) {
            List<String> names = (List)this.plugin.getGates().names().stream().filter(duplicates).filter((gate) -> {
               return gate.toLowerCase().contains(((String)args.get(args.size() - 1)).toLowerCase());
            }).collect(Collectors.toList());
            if (!names.isEmpty()) {
               return names;
            }
         }

         return (List)this.plugin.getGates().names().stream().filter(duplicates).collect(Collectors.toList());
      }
   }

   public List<String> findPlayers(List<String> args, int limit) {
      if (args.size() > limit) {
         return Collections.emptyList();
      } else {
         if (args.size() > 0) {
            List<String> names = (List)this.plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).filter((name) -> {
               return name.toLowerCase().contains(((String)args.get(args.size() - 1)).toLowerCase());
            }).collect(Collectors.toList());
            if (!names.isEmpty()) {
               return names;
            }
         }

         return (List)this.plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
      }
   }
}
