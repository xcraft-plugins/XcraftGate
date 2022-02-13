package de.xcraft.voronwe.xcraftgate;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class DataGate {
   private final XcraftGate plugin;
   private double x;
   private double y;
   private double z;
   private float pitch;
   private float yaw;
   private String worldName;
   private double toll = 0.0D;
   private boolean denysilent = false;
   private String gateName;
   private String gateTargetName = null;
   private DataGate gateTarget = null;

   public DataGate(XcraftGate instance, String name) {
      this.plugin = instance;
      this.gateName = name;
   }

   public Map<String, Object> toMap() {
      Map<String, Object> values = new HashMap();
      values.put("name", this.gateName);
      values.put("world", this.worldName);
      values.put("locX", this.x);
      values.put("locY", this.y);
      values.put("locZ", this.z);
      values.put("locP", this.pitch);
      values.put("locYaw", this.yaw);
      values.put("target", this.gateTargetName);
      values.put("toll", this.toll);
      values.put("denysilent", this.denysilent);
      return values;
   }

   public void setLocation(Location loc) {
      loc = Util.getSaneLocation(loc);
      this.setLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
   }

   public void setLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
      this.worldName = worldName;
      this.x = x;
      this.y = y;
      this.z = z;
      this.pitch = pitch;
      this.yaw = yaw;
   }

   public Location getLocation() {
      if (this.plugin.getServer().getWorld(this.worldName) == null) {
         return null;
      } else {
         Location ret = new Location(this.plugin.getServer().getWorld(this.worldName), this.x, this.y, this.z, this.yaw, this.pitch);
         return Util.getSaneLocation(ret);
      }
   }

   public Location getPortLocation() {
      if (this.plugin.getServer().getWorld(this.worldName) == null) {
         return null;
      } else {
         Location ret = new Location(this.plugin.getServer().getWorld(this.worldName), this.x, this.y + 1.5D, this.z, this.yaw, this.pitch);
         return Util.getSaneLocation(ret);
      }
   }

   public void setToll(Double toll) {
      this.toll = toll != null ? toll : 0.0D;
   }

   public double getToll() {
      return this.toll;
   }

   public void setDenySilent(Boolean denysilent) {
      this.denysilent = denysilent != null ? denysilent : false;
   }

   public boolean getDenySilent() {
      return this.denysilent;
   }

   public void setName(String newName) {
      this.gateName = newName;
   }

   public String getName() {
      return this.gateName;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getYaw() {
      return this.yaw;
   }

   public boolean hasTarget() {
      return this.gateTarget != null;
   }

   public DataGate getTarget() {
      return this.gateTarget;
   }

   public void linkTo(String gateName) {
      this.linkTo(this.plugin.getGates().get(gateName));
   }

   public void linkTo(String gateName, boolean save) {
      this.linkTo(this.plugin.getGates().get(gateName), save);
   }

   public void linkTo(DataGate gate) {
      this.linkTo(gate, true);
   }

   public void linkTo(DataGate gate, boolean save) {
      this.gateTarget = gate;
      if (gate != null) {
         this.gateTargetName = gate.getName();
      } else {
         this.gateTargetName = null;
      }

      if (save) {
         this.plugin.getGates().save();
      }

   }

   public void unlink() {
      this.gateTarget = null;
      this.gateTargetName = null;
   }

   private boolean checkWorld() {
      if (this.plugin.getWorlds().get(this.worldName) == null) {
         return false;
      } else {
         if (!this.plugin.getWorlds().get(this.worldName).isLoaded()) {
            this.plugin.getWorlds().get(this.worldName).load();
         }

         return true;
      }
   }

   public void portHere(Player player) {
      if (!this.checkWorld()) {
         player.sendMessage(ChatColor.RED + "Error: Target world '" + this.worldName + "' doesn't exist. Please alert your administrator!");
         this.plugin.justTeleported.put(player.getUniqueId(), Util.getSaneLocation(player.getLocation()));
      } else {
         this.plugin.justTeleported.put(player.getUniqueId(), this.getLocation());
         player.teleport(this.getLocation());
      }
   }

   public void portHere(PlayerMoveEvent event) {
      this.portHere(event.getPlayer());
   }

   public void portToTarget(Player player) {
      if (this.gateTarget != null) {
         this.gateTarget.portHere(player);
      }

   }

   public void portToTarget(PlayerMoveEvent event) {
      if (this.gateTarget != null) {
         this.gateTarget.portHere(event);
      }

   }

   public void sendInfo(CommandSender sender) {
      sender.sendMessage("Name: " + this.getName());
      if (this.plugin.getWorlds().get(this.getWorldName()).isLoaded()) {
         sender.sendMessage("Position: " + Util.getLocationString(this.getLocation()));
      } else {
         sender.sendMessage("Position: World " + this.getWorldName() + " is not loaded!");
      }

      String var10001 = this.getTarget() != null ? this.getTarget().getName() : "none";
      sender.sendMessage("Destination: " + var10001);
      if (this.plugin.getPluginManager().getEconomy() != null) {
         sender.sendMessage("Toll: " + this.plugin.getPluginManager().getEconomy().format(this.toll));
      }

      sender.sendMessage("Deny usage silently: " + (this.denysilent ? "Yes" : "No"));
      sender.sendMessage("Permission-Node: XcraftGate.use." + this.getName());
   }
}
