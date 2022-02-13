package de.xcraft.voronwe.xcraftgate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.yaml.snakeyaml.Yaml;

public class ListenerPlayer implements Listener {
   private final XcraftGate plugin;
   private final Map<UUID, String> playerDiedInWorld = new HashMap();
   private Map<String, String> playerLeftInWorld = new HashMap();

   public ListenerPlayer(XcraftGate instance) {
      this.plugin = instance;
   }

   public void loadPlayers() {
      File configFile = this.plugin.getConfigFile("playerWorlds.yml");

      try {
         Yaml yaml = new Yaml();
         if ((this.playerLeftInWorld = (Map)yaml.load(new FileInputStream(configFile))) == null) {
            this.playerLeftInWorld = new HashMap();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void savePlayers() {
      File configFile = this.plugin.getConfigFile("playerWorlds.yml");
      Yaml yaml = new Yaml();
      String dump = yaml.dump(this.playerLeftInWorld);

      try {
         FileOutputStream fh = new FileOutputStream(configFile);
         (new PrintStream(fh)).println(dump);
         fh.flush();
         fh.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   public void onPlayerLogin(PlayerLoginEvent event) {
      if (event.getResult() == Result.ALLOWED) {
         Player player = event.getPlayer();
         String worldName = (String)this.playerLeftInWorld.get(player.getUniqueId().toString());
         DataWorld world = this.plugin.getWorlds().get(worldName);
         XcraftGate var10000 = this.plugin;
         String var10001 = player.getName();
         var10000.logInfo("Player " + var10001 + " (" + player.getUniqueId() + ") trying to join in world " + worldName);
         if (world != null && !world.isLoaded()) {
            world.load();
         }

         if (worldName == null) {
            this.playerLeftInWorld.put(event.getPlayer().getUniqueId().toString(), event.getPlayer().getWorld().getName());
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      DataWorld thisWorld = this.plugin.getWorlds().get(player.getWorld());
      XcraftGate var10000 = this.plugin;
      String var10001 = player.getName();
      var10000.logInfo("Player " + var10001 + " logged in in world " + thisWorld.getName() + " with game mode " + thisWorld.getGameMode().toString());
      if (!player.hasPermission("XcraftGate.world.nogamemodechange")) {
         player.setGameMode(thisWorld.getGameMode());
      }

      if (!player.hasPermission("XcraftGate.world.announcelogin")) {
         event.setJoinMessage((String)null);
      }

   }

   @EventHandler
   public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
      DataWorld fromWorld = this.plugin.getWorlds().get(event.getFrom());
      DataWorld toWorld = this.plugin.getWorlds().get(event.getPlayer().getWorld());
      if (this.plugin.getConfig().getBoolean("invsep.enabled") && !fromWorld.getInventoryGroup().equalsIgnoreCase(toWorld.getInventoryGroup())) {
         this.plugin.getInventoryManager().changeInventory(event.getPlayer(), fromWorld, toWorld);
      }

      if (event.getPlayer().hasPermission("XcraftGate.world.info")) {
         Player var10000 = event.getPlayer();
         ChatColor var10001 = ChatColor.AQUA;
         var10000.sendMessage(var10001 + "World changed from " + fromWorld.getName() + " to " + toWorld.getName());
      }

      if (!event.getPlayer().hasPermission("XcraftGate.world.nogamemodechange")) {
         event.getPlayer().setGameMode(toWorld.getGameMode());
      }

      this.playerLeftInWorld.put(event.getPlayer().getUniqueId().toString(), event.getPlayer().getWorld().getName());
   }

   @EventHandler
   public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
      if (this.plugin.getConfig().getBoolean("invsep.enabled")) {
         this.plugin.getInventoryManager().changeInventroy(event.getPlayer(), event.getPlayer().getGameMode(), event.getNewGameMode(), this.plugin.getWorlds().get(event.getPlayer().getWorld()));
      }

   }

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      Location location = event.getTo();
      DataWorld worldData = this.plugin.getWorlds().get(location);
      if (worldData != null) {
         int border = worldData.getBorder();
         Player player = event.getPlayer();
         if (border > 0) {
            double x = location.getX();
            double z = location.getZ();
            if (Math.abs(x) >= (double)border || Math.abs(z) >= (double)border) {
               x = Math.abs(x) >= (double)border ? (double)(x > 0.0D ? border - 1 : -border + 1) : x;
               z = Math.abs(z) >= (double)border ? (double)(z > 0.0D ? border - 1 : -border + 1) : z;
               Location back = new Location(location.getWorld(), x, location.getY(), z, location.getYaw(), location.getPitch());
               event.setTo(back);
               player.sendMessage(ChatColor.RED + "You reached the border of this world.");
               return;
            }
         }

         Location portTo = (Location)this.plugin.justTeleported.get(player.getUniqueId());
         Location portFrom = (Location)this.plugin.justTeleportedFrom.get(player.getUniqueId());
         if (portTo != null && portFrom == null) {
            this.plugin.justTeleported.remove(player.getUniqueId());
         }

         if (portTo == null && portFrom != null) {
            this.plugin.justTeleportedFrom.remove(player.getUniqueId());
         }

         if (portTo != null && portFrom != null) {
            if ((Math.floor(portTo.getX()) != Math.floor(location.getX()) || Math.floor(portTo.getZ()) != Math.floor(location.getZ())) && (Math.floor(portFrom.getX()) != Math.floor(location.getX()) || Math.floor(portFrom.getZ()) != Math.floor(location.getZ()))) {
               this.plugin.justTeleported.remove(player.getUniqueId());
               this.plugin.justTeleportedFrom.remove(player.getUniqueId());
            }

         } else {
            DataGate gate = this.plugin.getGates().getByLocation(location);
            if (gate != null) {
               label124: {
                  if (this.plugin.getPluginManager().getPermissions() == null) {
                     if (player.hasPermission("XcraftGate.use." + gate.getName())) {
                        break label124;
                     }
                  } else if (this.plugin.getPluginManager().getPermissions().has(player, "XcraftGate.use." + gate.getName())) {
                     break label124;
                  }

                  this.setJustTeleported(player);
                  if (!gate.getDenySilent()) {
                     player.sendMessage(ChatColor.RED + "You're not allowed to use this gate!");
                  }

                  return;
               }

               this.plugin.justTeleportedFrom.put(player.getUniqueId(), gate.getLocation());
               if (this.plugin.getPluginManager().getEconomy() != null && gate.getToll() > 0.0D) {
                  ChatColor var10001;
                  if (this.plugin.getPluginManager().getEconomy().has(player, gate.getToll())) {
                     this.plugin.getPluginManager().getEconomy().withdrawPlayer(player, gate.getToll());
                     var10001 = ChatColor.AQUA;
                     player.sendMessage(var10001 + "Took " + this.plugin.getPluginManager().getEconomy().format(gate.getToll()) + " from your account for using this gate.");
                     gate.portToTarget(event);
                  } else if (!gate.getDenySilent()) {
                     var10001 = ChatColor.RED;
                     player.sendMessage(var10001 + "You don't have enough money to use this gate (Requires: " + this.plugin.getPluginManager().getEconomy().format(gate.getToll()) + ")");
                     this.setJustTeleported(player);
                  }
               } else {
                  gate.portToTarget(event);
               }
            }

         }
      }
   }

   private void setJustTeleported(Player player) {
      this.plugin.justTeleported.put(player.getUniqueId(), Util.getSaneLocation(player.getLocation()));
   }

   @EventHandler
   public void onPlayerTeleport(PlayerTeleportEvent event) {
      if (this.plugin.config.getBoolean("fixes.chunkRefreshOnTeleport")) {
         Location targetLoc = event.getTo();
         World targetWorld = targetLoc.getWorld();
         Chunk targetChunk = targetWorld.getChunkAt(targetLoc);
         targetWorld.refreshChunk(targetChunk.getX(), targetChunk.getZ());
      }

   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      this.playerDiedInWorld.put(event.getEntity().getUniqueId(), event.getEntity().getWorld().getName());
      DataWorld world = this.plugin.getWorlds().get(event.getEntity().getWorld());
      if (world != null) {
         if (!world.getAnnouncePlayerDeath()) {
            event.setDeathMessage("");
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      DataWorld worldDied = this.plugin.getWorlds().get((String)this.playerDiedInWorld.get(event.getPlayer().getUniqueId()));
      if (worldDied == null) {
         XcraftGate var10000 = this.plugin;
         String var10001 = event.getPlayer().getName();
         var10000.logInfo("Player " + var10001 + " died, but i don't know where?! (" + event.getPlayer().getWorld().getName() + ")");
      } else {
         switch(worldDied.getRespawnLocation()) {
         case WORLDSPAWN:
            event.setRespawnLocation(worldDied.getWorld().getSpawnLocation());
            break;
         case BEDSPAWN:
            if (event.getPlayer().getBedSpawnLocation() != null) {
               event.setRespawnLocation(event.getPlayer().getBedSpawnLocation());
            } else {
               event.setRespawnLocation(worldDied.getWorld().getSpawnLocation());
            }
            break;
         case WORLD:
            String respawnWorldName = worldDied.getRespawnWorldName();
            DataWorld respawnWorld;
            if (respawnWorldName != null && this.plugin.getWorlds().get(respawnWorldName) != null) {
               respawnWorld = this.plugin.getWorlds().get(respawnWorldName);
            } else {
               respawnWorld = worldDied;
            }

            event.setRespawnLocation(respawnWorld.getWorld().getSpawnLocation());
         }

      }
   }

   @EventHandler
   public void onPlayerLogout(PlayerQuitEvent event) {
      if (!event.getPlayer().hasPermission("XcraftGate.world.announcelogin")) {
         event.setQuitMessage((String)null);
      }

   }
}
