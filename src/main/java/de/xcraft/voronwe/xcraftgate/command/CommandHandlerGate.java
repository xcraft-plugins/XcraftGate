package de.xcraft.voronwe.xcraftgate.command;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.gate.CreateGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.DeleteGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.GateInfoCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.LinkGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.ListGatesCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.ListnearGatesCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.ListsoloGatesCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.LoopGatesCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.MoveGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.ReloadGatesCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.RenameGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.SetGateDenySilentCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.SetGateTollCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.UnlinkGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.UnloopGateCommand;
import de.xcraft.voronwe.xcraftgate.command.gate.WarpGateCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CommandHandlerGate extends CommandHelper implements TabExecutor {
   private final Map<String, Supplier<GateSubcommand>> subcommands = new HashMap();
   private final Map<String, String> permNodes = new HashMap();

   public CommandHandlerGate(XcraftGate instance) {
      super(instance);
      this.permNodes.put("create", "create");
      this.permNodes.put("move", "create");
      this.permNodes.put("rename", "create");
      this.permNodes.put("link", "link");
      this.permNodes.put("loop", "link");
      this.permNodes.put("unlink", "unlink");
      this.permNodes.put("unloop", "unlink");
      this.permNodes.put("delete", "delete");
      this.permNodes.put("info", "info");
      this.permNodes.put("list", "info");
      this.permNodes.put("listnear", "info");
      this.permNodes.put("listsolo", "info");
      this.permNodes.put("warp", "warp");
      this.permNodes.put("reload", "reload");
      this.permNodes.put("setdenysilent", "denysilent");
      this.permNodes.put("settoll", "toll");
      this.subcommands.put("create", () -> {
         return new CreateGateCommand(this.plugin);
      });
      this.subcommands.put("move", () -> {
         return new MoveGateCommand(this.plugin);
      });
      this.subcommands.put("rename", () -> {
         return new RenameGateCommand(this.plugin);
      });
      this.subcommands.put("link", () -> {
         return new LinkGateCommand(this.plugin);
      });
      this.subcommands.put("loop", () -> {
         return new LoopGatesCommand(this.plugin);
      });
      this.subcommands.put("unlink", () -> {
         return new UnlinkGateCommand(this.plugin);
      });
      this.subcommands.put("unloop", () -> {
         return new UnloopGateCommand(this.plugin);
      });
      this.subcommands.put("delete", () -> {
         return new DeleteGateCommand(this.plugin);
      });
      this.subcommands.put("info", () -> {
         return new GateInfoCommand(this.plugin);
      });
      this.subcommands.put("list", () -> {
         return new ListGatesCommand(this.plugin);
      });
      this.subcommands.put("listnear", () -> {
         return new ListnearGatesCommand(this.plugin);
      });
      this.subcommands.put("listsolo", () -> {
         return new ListsoloGatesCommand(this.plugin);
      });
      this.subcommands.put("warp", () -> {
         return new WarpGateCommand(this.plugin);
      });
      this.subcommands.put("reload", () -> {
         return new ReloadGatesCommand(this.plugin);
      });
      this.subcommands.put("setdenysilent", () -> {
         return new SetGateDenySilentCommand(this.plugin);
      });
      this.subcommands.put("settoll", () -> {
         return new SetGateTollCommand(this.plugin);
      });
   }

   public void printUsage() {
      ChatColor var10001 = ChatColor.GRAY;
      this.sender.sendMessage(var10001 + this.plugin.getNameBrackets() + ChatColor.GOLD + "by Engelier, maintained by Voronwe");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate info <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate create <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate move <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate rename <name> <new_name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate link <name1> <name2>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate loop <name1> <name2>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate unlink <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate unloop <name1> <name2>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate delete <name>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate settoll <name> <amount>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate setdenysilent <name> <true|false>");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate list");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate listnear [radius]");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate listsolo");
      this.sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + "/gate warp <name> [player]");
   }

   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
      this.sender = sender;
      this.player = sender instanceof Player ? (Player)sender : null;
      if (!this.isPermitted("gate", args.length > 0 ? (String)this.permNodes.get(args[0]) : null)) {
         this.error("You don't have permission to use this command");
         return true;
      } else if (args.length == 0) {
         this.printUsage();
         return true;
      } else {
         GateSubcommand subCommand = (GateSubcommand)((Supplier)this.subcommands.get(args[0].toLowerCase())).get();
         if (this.player == null && !subCommand.executableAsConsole()) {
            this.error("/gate " + args[0] + " cannot be used from the console");
            return true;
         } else {
            if (subCommand == null) {
               this.printUsage();
               this.error("Unkown gate command: " + args[0].toLowerCase());
            } else {
               List<String> largs = Arrays.asList(args);
               largs = largs.subList(1, largs.size());
               String gateName = largs.size() > 0 ? (String)largs.get(0) : null;
               subCommand.sender = sender;
               subCommand.execute(gateName, (List)(largs.size() > 1 ? largs.subList(1, largs.size()) : new ArrayList()));
            }

            return true;
         }
      }
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      this.player = sender instanceof Player ? (Player)sender : null;
      if (!this.isPermitted("gate", args.length > 0 ? (String)this.permNodes.get(args[0]) : null)) {
         return Collections.emptyList();
      } else {
         boolean isPlayer = sender instanceof Player;
         if (args.length == 0) {
            return (List)this.subcommands.keySet().stream().filter((cmd) -> {
               return this.isPermitted("gate", (String)this.permNodes.get(cmd));
            }).filter((cmd) -> {
               return isPlayer || ((GateSubcommand)((Supplier)this.subcommands.get(cmd)).get()).executableAsConsole();
            }).collect(Collectors.toList());
         } else if (args.length == 1) {
            List<String> commands = (List)this.subcommands.keySet().stream().filter((cmd) -> {
               return cmd.contains(args[0]);
            }).filter((cmd) -> {
               return this.isPermitted("gate", (String)this.permNodes.get(cmd));
            }).filter((cmd) -> {
               return isPlayer || ((GateSubcommand)((Supplier)this.subcommands.get(cmd)).get()).executableAsConsole();
            }).collect(Collectors.toList());
            return commands.isEmpty() ? (List)this.subcommands.keySet().stream().filter((cmd) -> {
               return this.isPermitted("gate", (String)this.permNodes.get(cmd));
            }).filter((cmd) -> {
               return isPlayer || ((GateSubcommand)((Supplier)this.subcommands.get(cmd)).get()).executableAsConsole();
            }).collect(Collectors.toList()) : commands;
         } else {
            GateSubcommand subCommand = (GateSubcommand)((Supplier)this.subcommands.get(args[0].toLowerCase())).get();
            return subCommand != null && (isPlayer || subCommand.executableAsConsole()) ? subCommand.tabComplete(Arrays.asList(args).subList(1, args.length)) : Collections.emptyList();
         }
      }
   }
}
