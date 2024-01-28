package de.xcraft.voronwe.xcraftgate;

import de.xcraft.voronwe.xcraftgate.command.CommandHandlerGate;
import de.xcraft.voronwe.xcraftgate.command.CommandHandlerWorld;
import de.xcraft.voronwe.xcraftgate.generator.GeneratorType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class XcraftGate extends JavaPlugin {
   private final ListenerServer pluginListener = new ListenerServer(this);
   private final ListenerPlayer playerListener = new ListenerPlayer(this);
   private final ListenerCreature creatureListener = new ListenerCreature(this);
   private final ListenerEntity entityListener = new ListenerEntity(this);
   private final ListenerWeather weatherListener = new ListenerWeather(this);
   private final ListenerWorld worldListener = new ListenerWorld(this);
   private final InventoryManager inventoryManager = new InventoryManager(this);
   private PluginManager pm = null;
   private final SetWorld worlds = new SetWorld(this);
   private final SetGate gates = new SetGate(this);
   public final Map<UUID, Location> justTeleported = new HashMap();
   public final Map<UUID, Location> justTeleportedFrom = new HashMap();
   public YamlConfiguration config = null;
   private final Logger log = Logger.getLogger("Minecraft");
   public final Properties serverconfig = new Properties();
   private boolean forceUpgrade;

   public void saveAll() {
      if (!this.forceUpgrade) {
         this.playerListener.savePlayers();
         this.inventoryManager.save();
         this.gates.save();
         this.worlds.save();
      }

   }

   public void onDisable() {
      this.getServer().getScheduler().cancelTasks(this);
      this.saveAll();
   }

   public void onEnable() {
      this.playerListener.loadPlayers();
      this.inventoryManager.load();
      this.pm = new PluginManager(this);
      this.pm.registerEvents(this.creatureListener);
      this.pm.registerEvents(this.entityListener);
      this.pm.registerEvents(this.playerListener);
      this.pm.registerEvents(this.pluginListener);
      this.pm.registerEvents(this.weatherListener);
      this.pm.registerEvents(this.worldListener);
      File serverconfigFile = new File("server.properties");
      if (!serverconfigFile.exists()) {
         this.log.severe(this.getNameBrackets() + "unable to load server.properties.");
      } else {
         try {
            this.serverconfig.load(new FileInputStream(serverconfigFile));
         } catch (Exception var6) {
            Logger var10000 = this.log;
            String var10001 = this.getNameBrackets();
            var10000.severe(var10001 + "error loading " + serverconfigFile);
            var6.printStackTrace();
         }
      }

      this.config = this.getConfig(this.getConfigFile("config.yml"));

      try {
         this.setConfigDefaults();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.worlds.load();
      this.gates.load();
      this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new XcraftGate.RunCreatureLimit(), 600L, 600L);
      this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new XcraftGate.RunTimeFrozen(), 200L, 200L);
      this.getServer().getScheduler().runTaskTimerAsynchronously(this, this::saveAll, 12000L, 12000L);
      this.getServer().getScheduler().scheduleSyncDelayedTask(this, this.pm);

      try {
         CommandHandlerGate handlerGate = new CommandHandlerGate(this);
         PluginCommand gateCommand = this.getCommand("gate");
         gateCommand.setExecutor(handlerGate);
         gateCommand.setTabCompleter(handlerGate);
         this.getCommand("gworld").setExecutor(new CommandHandlerWorld(this));
      } catch (Exception var4) {
         this.logWarning("getCommand().setExecutor() failed! Seems I got enabled by another plugin. Nag the bukkit team about this!");
      }

   }

   private boolean isForceUpgradeMode() {
      return System.getProperty("sun.java.command", "").contains("--forceUpgrade");
   }

   public YamlConfiguration getConfig() {
      return this.config;
   }

   public YamlConfiguration getConfig(String fileName) {
      return this.getConfig(this.getConfigFile(fileName));
   }

   public YamlConfiguration getConfig(File file) {
      YamlConfiguration ret = new YamlConfiguration();

      try {
         ret.load(file);
      } catch (IOException | InvalidConfigurationException var4) {
         var4.printStackTrace();
      }

      return ret;
   }

   public File getConfigFile(String fileName) {
      File configFile = new File(this.getDataFolder(), fileName);
      if (!configFile.exists()) {
         try {
            this.getDataFolder().mkdir();
            this.getDataFolder().setWritable(true);
            this.getDataFolder().setExecutable(true);
            configFile.createNewFile();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return configFile;
   }

   private void setConfigDefaults() throws IOException {
      this.config.options().copyDefaults(true);
      this.config.addDefault("dynworld.checkInterval", 60);
      this.config.addDefault("dynworld.maxInactiveTime", 300);
      this.config.addDefault("invsep.enabled", true);
      this.config.addDefault("invsep.exp", true);
      this.config.addDefault("invsep.health", true);
      this.config.addDefault("invsep.food", true);
      this.config.addDefault("invsep.ec", false);
      this.config.addDefault("invsep.potion", false);
      this.config.addDefault("fixes.chunkRefreshOnTeleport", false);
      this.logInfo("Saving default config.");
      this.config.save(this.getConfigFile("config.yml"));
   }

   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
      PluginCommand gworldCommand;
      if (cmd.getName().equalsIgnoreCase("gate")) {
         gworldCommand = this.getCommand("gate");
         gworldCommand.setExecutor(new CommandHandlerGate(this));
         gworldCommand.execute(sender, commandLabel, args);
         return true;
      } else if (cmd.getName().equalsIgnoreCase("gworld")) {
         gworldCommand = this.getCommand("gworld");
         gworldCommand.setExecutor(new CommandHandlerWorld(this));
         gworldCommand.execute(sender, commandLabel, args);
         return true;
      } else {
         return false;
      }
   }

   public String getNameBrackets() {
      return "[" + this.getDescription().getName() + "] ";
   }

   public SetWorld getWorlds() {
      return this.worlds;
   }

   public SetGate getGates() {
      return this.gates;
   }

   public PluginManager getPluginManager() {
      return this.pm;
   }

   public InventoryManager getInventoryManager() {
      return this.inventoryManager;
   }

   public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
      GeneratorType[] var3 = GeneratorType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         GeneratorType thisGen = var3[var5];
         if (thisGen.toString().equalsIgnoreCase(id)) {
            return thisGen.getChunkGenerator((String)null);
         }
      }

      return null;
   }

   public void logInfo(String info) {
      Logger var10000 = this.log;
      String var10001 = this.getNameBrackets();
      var10000.info(var10001 + info);
   }

   public void logWarning(String warning) {
      Logger var10000 = this.log;
      String var10001 = this.getNameBrackets();
      var10000.info(var10001 + warning);
   }

   class RunCreatureLimit implements Runnable {
      public void run() {
         Iterator var1 = XcraftGate.this.worlds.iterator();

         while(var1.hasNext()) {
            DataWorld thisWorld = (DataWorld)var1.next();
            thisWorld.checkCreatureLimit();
         }

      }
   }

   class RunTimeFrozen implements Runnable {
      public void run() {
         Iterator var1 = XcraftGate.this.worlds.iterator();

         while(var1.hasNext()) {
            DataWorld thisWorld = (DataWorld)var1.next();
            thisWorld.resetFrozenTimeIfFrozen();
         }

      }
   }
}
