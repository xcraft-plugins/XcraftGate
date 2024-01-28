package de.xcraft.voronwe.xcraftgate.command;

import de.xcraft.voronwe.xcraftgate.DataWorld;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.world.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CommandHandlerWorld extends CommandHelper implements TabExecutor {
   private final Map<String, Supplier<WorldSubcommand>> subcommands = new HashMap();
   private final Map<String, String> permNodes = new HashMap();

   public CommandHandlerWorld(XcraftGate instance) {
      super(instance);
      this.permNodes.put("create", "create");
      this.permNodes.put("delete", "delete");
      this.permNodes.put("warpto", "warp");
      this.permNodes.put("setborder", "setborder");
      this.permNodes.put("setcreaturelimit", "setcreaturelimit");
      this.permNodes.put("allowanimals", "setcreaturelimit");
      this.permNodes.put("allowmonsters", "setcreaturelimit");
      this.permNodes.put("suppresshealthregain", "setcreaturelimit");
      this.permNodes.put("suppresshunger", "setcreaturelimit");
      this.permNodes.put("allowpvp", "pvp");
      this.permNodes.put("allowweatherchange", "weather");
      this.permNodes.put("setweather", "weather");
      this.permNodes.put("timefrozen", "time");
      this.permNodes.put("settime", "time");
      this.permNodes.put("info", "info");
      this.permNodes.put("list", "info");
      this.permNodes.put("listenv", "info");
      this.permNodes.put("listplayers", "info");
      this.permNodes.put("load", "load");
      this.permNodes.put("unload", "load");
      this.permNodes.put("keepspawninmemory", "load");
      this.permNodes.put("setdifficulty", "difficulty");
      this.permNodes.put("setannouncedeath", "difficulty");
      this.permNodes.put("setgamemode", "gamemode");
      this.permNodes.put("setloginmessage", "gamemode");
      this.permNodes.put("setspawn", "spawn");
      this.permNodes.put("setrespawnlocation", "spawn");
      this.permNodes.put("setinventorygroup", "inventory");
      this.subcommands.put("create", () -> {
         return new CreateWorldCommand(this.plugin);
      });
      this.subcommands.put("info", () -> {
         return new WorldInfoCommand(this.plugin);
      });
      this.subcommands.put("listenv", () -> {
         return new ListEnvironmentsCommand(this.plugin);
      });
      this.subcommands.put("list", () -> {
         return new ListWorldsCommand(this.plugin);
      });
      this.subcommands.put("delete", () -> {
         return new DeleteWorldCommand(this.plugin);
      });
      this.subcommands.put("warpto", () -> {
         return new WarptoWorldCommand(this.plugin);
      });
      this.subcommands.put("setborder", () -> {
         return new SetWorldBorderCommand(this.plugin);
      });
      this.subcommands.put("setcreaturelimit", () -> {
         return new SetWorldCreatureLimitCommand(this.plugin);
      });
      this.subcommands.put("allowanimals", () -> {
         return new SimpleWorldProperty(this.plugin, "allowanimals", "Animal spawn", DataWorld::setAllowAnimals);
      });
      this.subcommands.put("allowmonsters", () -> {
         return new SimpleWorldProperty(this.plugin, "allowmonsters", "Monster spawn", DataWorld::setAllowMonsters);
      });
      this.subcommands.put("allowpvp", () -> {
         return new SimpleWorldProperty(this.plugin, "allowpvp", "PvP", DataWorld::setAllowPvP);
      });
      this.subcommands.put("allowweatherchange", () -> {
         return new SimpleWorldProperty(this.plugin, "allowweatherchange", "Weather changes", DataWorld::setAllowWeatherChange);
      });
      this.subcommands.put("timefrozen", () -> {
         return new SimpleWorldProperty(this.plugin, "timefrozen", "Regular time resets", DataWorld::setTimeFrozen);
      });
      this.subcommands.put("settime", () -> {
         return new SetWorldTimeCommand(this.plugin);
      });
      this.subcommands.put("suppresshealthregain", () -> {
         return new SimpleWorldProperty(this.plugin, "suppresshealthregain", "No natural regeneration", DataWorld::setSuppressHealthRegain);
      });
      this.subcommands.put("suppresshunger", () -> {
         return new SimpleWorldProperty(this.plugin, "suppresshunger", "Infinite saturation", DataWorld::setSuppressHunger);
      });
      this.subcommands.put("listplayers", () -> {
         return new ListWorldsPlayersCommand(this.plugin);
      });
      this.subcommands.put("load", () -> {
         return new LoadWorldCommand(this.plugin);
      });
      this.subcommands.put("unload", () -> {
         return new UnloadWorldCommand(this.plugin);
      });
      this.subcommands.put("keepspawninmemory", () -> {
         return new SimpleWorldProperty(this.plugin, "keepspawninmemory", "Keeping spawn chunks loaded", DataWorld::setKeepSpawnInMemory);
      });
      this.subcommands.put("setdifficulty", () -> {
         return new SetWorldDifficultyCommand(this.plugin);
      });
      this.subcommands.put("setgamemode", () -> {
         return new SetWorldGameModeCommand(this.plugin);
      });
      this.subcommands.put("setloginmessage", () -> {
         return new SetWorldLoginMessageCommand(this.plugin);
      });
      this.subcommands.put("setannouncedeath", () -> {
         return new SimpleWorldProperty(this.plugin, "setannouncedeath", "Death announcements", DataWorld::setAnnouncePlayerDeath);
      });
      this.subcommands.put("setspawn", () -> {
         return new SetWorldSpawnCommand(this.plugin);
      });
      this.subcommands.put("setrespawnlocation", () -> {
         return new SetWorldRespawnLocationCommand(this.plugin);
      });
      this.subcommands.put("setinventorygroup", () -> {
         return new SetWorldInventoryGroupCommand(this.plugin);
      });
   }

   public void printUsage() {
      ChatColor var10001 = ChatColor.GRAY;
      this.sender.sendMessage(var10001 + this.plugin.getNameBrackets());
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld list");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld info <world>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld listenv");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld listplayers <world>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld create <name> [<environment> [<seed> [settings]]]");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld delete <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld warpto <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setborder <world> <#>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setcreaturelimit <world> <#>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld allowanimals <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld allowmonsters <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld allowpvp <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld allowweatherchange <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld timefrozen <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld settime <world> <sunrise|noon|sunset|midnight>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setdifficulty <world> <peaceful|easy|normal|hard>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setannouncedeath <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setgamemode <world> <survival|creative|adventure>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setloginmessage <world> [message]");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setspawn <world>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setrespawnlocation <world> <worldspawn|bedspawn|world <worldname>>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld suppresshealthregain <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld suppresshunger <world> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld setinventorygroup <world> <groupname>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gworld keepspawninmemory <world> <true|false>");
   }

   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
      this.sender = sender;
      this.player = sender instanceof Player ? (Player)sender : null;
      if (!this.isPermitted("world", args.length > 0 ? (String)this.permNodes.get(args[0]) : null)) {
         this.error("You don't have permission to use this command");
         return true;
      } else if (args.length == 0) {
         this.printUsage();
         return true;
      } else {
         WorldSubcommand subcommand = (WorldSubcommand)((Supplier)this.subcommands.get(args[0].toLowerCase())).get();
         if (subcommand == null) {
            this.printUsage();
            this.error("Unkown gworld command: " + args[0].toLowerCase());
            return true;
         } else if (this.player == null && !subcommand.executableAsConsole()) {
            this.error("/gworld " + args[0].toLowerCase() + " cannot be used from the console");
            return true;
         } else {
            List<String> largs = Arrays.asList(args);
            largs = largs.subList(1, largs.size());
            subcommand.execute(sender, largs.size() > 0 ? (String)largs.get(0) : null, (List)(largs.size() > 1 ? largs.subList(1, largs.size()) : new ArrayList()));
            return true;
         }
      }
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      this.player = sender instanceof Player ? (Player)sender : null;
      if (this.player == null && args.length > 0 && (args[0].equalsIgnoreCase("warpto") || args[0].equalsIgnoreCase("setspawn"))) {
         return Collections.emptyList();
      } else if (!this.isPermitted("gworld", args.length > 0 ? (String)this.permNodes.get(args[0]) : null)) {
         return Collections.emptyList();
      } else if (args.length == 0) {
         return (List)this.subcommands.keySet().stream().filter((cmd) -> {
            return this.player != null || ((WorldSubcommand)((Supplier)this.subcommands.get(cmd)).get()).executableAsConsole();
         }).filter((cmd) -> {
            return this.isPermitted("gworld", (String)this.permNodes.get(cmd));
         }).collect(Collectors.toList());
      } else if (args.length == 1) {
         List<String> commands = (List)this.subcommands.keySet().stream().filter((cmd) -> {
            return this.player != null || ((WorldSubcommand)((Supplier)this.subcommands.get(cmd)).get()).executableAsConsole();
         }).filter((cmd) -> {
            return cmd.contains(args[0]);
         }).filter((cmd) -> {
            return this.isPermitted("gworld", (String)this.permNodes.get(cmd));
         }).collect(Collectors.toList());
         return commands.isEmpty() ? (List)this.subcommands.keySet().stream().filter((cmd) -> {
            return this.player != null || ((WorldSubcommand)((Supplier)this.subcommands.get(cmd)).get()).executableAsConsole();
         }).filter((cmd) -> {
            return this.isPermitted("gworld", (String)this.permNodes.get(cmd));
         }).collect(Collectors.toList()) : commands;
      } else {
         WorldSubcommand subCommand = (WorldSubcommand)((Supplier)this.subcommands.get(args[0].toLowerCase())).get();
         return subCommand == null ? Collections.emptyList() : subCommand.tabComplete(Arrays.asList(args).subList(1, args.length));
      }
   }
}
