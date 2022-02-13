package de.xcraft.voronwe.xcraftgate.command;

import de.xcraft.voronwe.xcraftgate.XcraftGate;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandHelper {
   protected final XcraftGate plugin;
   protected CommandSender sender = null;
   protected Player player;

   public CommandHelper(XcraftGate instance) {
      this.plugin = instance;
   }

   public boolean executableAsConsole() {
      return true;
   }

   public void reply(String message) {
      ChatColor var10001 = ChatColor.LIGHT_PURPLE;
      this.sender.sendMessage(var10001 + this.plugin.getNameBrackets() + ChatColor.DARK_AQUA + message);
   }

   public void error(String message) {
      this.sender.sendMessage(ChatColor.RED + "Error: " + message);
   }

   public boolean isPermitted(String command, String subcommand) {
      if (this.player == null) {
         return true;
      } else if (this.plugin.getPluginManager().getPermissions() != null) {
         return subcommand != null ? this.plugin.getPluginManager().getPermissions().has(this.player, "XcraftGate." + command + "." + subcommand) : this.plugin.getPluginManager().getPermissions().has(this.player, "XcraftGate." + command);
      } else {
         return subcommand != null ? this.player.hasPermission("XcraftGate." + command + "." + subcommand) : this.player.hasPermission("XcraftGate." + command);
      }
   }

   public List<String> tabComplete(List<String> args) {
      return null;
   }
}
