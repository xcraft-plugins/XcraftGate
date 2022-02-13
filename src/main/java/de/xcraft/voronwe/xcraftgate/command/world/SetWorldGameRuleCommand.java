package de.xcraft.voronwe.xcraftgate.command.world;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.WorldSubcommand;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class SetWorldGameRuleCommand extends WorldSubcommand {
   public SetWorldGameRuleCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(CommandSender sender, String worldName, List<String> args) {
      this.sender = sender;
      if (worldName == null) {
         this.error("No world given.");
         this.reply("Usage: /gworld setgamerule <worldname> <rulename> <value>");
      } else if (!this.hasWorld(worldName)) {
         this.reply("World not found: " + worldName);
      } else if (args.size() != 2) {
         this.error("Wrong argument count.");
         this.reply("Usage: /gworld setgamerule <worldname> <rulename> <value>");
      } else {
         String rule = (String)args.get(0);
         String value = (String)args.get(1);
         GameRule gameRule = GameRule.getByName(rule);
         if (gameRule == null) {
            this.reply("Unknown gamerule '" + rule + "'");
            return;
         }

         DataWorld dataWorld = this.getWorld(worldName);
         boolean isLoaded = dataWorld.isLoaded();
         if (!isLoaded) {
            dataWorld.load();
         }

         World world = dataWorld.getWorld();
         if (gameRule.getType() == Boolean.class) {
            if ("true".equalsIgnoreCase(value)) {
               world.setGameRule(gameRule, true);
            } else {
               if (!"false".equalsIgnoreCase(value)) {
                  this.error("Value must be a boolean!");
                  return;
               }

               if (gameRule.equals(GameRule.DO_DAYLIGHT_CYCLE)) {
                  this.reply("&cYou are disabling daylight cycle. To prevent other change, for example from beds, use: /gworld timefrozen " + worldName);
               }

               world.setGameRule(gameRule, false);
            }
         } else if (gameRule.getType() == Integer.class) {
            int newValue;
            try {
               newValue = Integer.parseInt(value);
            } catch (NumberFormatException var12) {
               this.error("Value must be a number!");
               return;
            }

            world.setGameRule(gameRule, newValue);
         } else {
            world.setGameRuleValue(rule, value);
         }

         this.reply("GameRule '" + rule + "' for world " + worldName + " set to " + value);
      }

   }

   public List<String> tabCompleteFurther(List<String> args) {
      if (args.size() <= 1) {
         return (List)Arrays.stream(GameRule.values()).map(GameRule::getName).filter((name) -> {
            return args.size() == 0 || name.toLowerCase().contains(((String)args.get(0)).toLowerCase());
         }).collect(Collectors.toList());
      } else if (args.size() <= 2) {
         GameRule<?> gameRule = GameRule.getByName((String)args.get(0));
         return gameRule == null ? Collections.emptyList() : (gameRule.getType() == Boolean.class ? Arrays.asList("true", "false") : (gameRule.getType() == Integer.class ? Arrays.asList("1", "2", "4", "8", "16", "32", "64", "128") : null));
      } else {
         return Collections.emptyList();
      }
   }
}
