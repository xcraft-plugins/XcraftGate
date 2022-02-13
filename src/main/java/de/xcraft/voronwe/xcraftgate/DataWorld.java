package de.xcraft.voronwe.xcraftgate;

import de.xcraft.voronwe.xcraftgate.generator.GeneratorType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.generator.ChunkGenerator;

public class DataWorld {
   private final XcraftGate plugin;
   private final GeneratorType generator;
   private final WorldType worldType;
   private String name;
   private Environment environment;
   private boolean allowAnimals;
   private boolean allowMonsters;
   private boolean allowPvP;
   private boolean allowWeatherChange;
   private int creatureLimit;
   private int border;
   private DataWorld.Weather setWeather;
   private long setTime;
   private boolean timeFrozen;
   private boolean suppressHealthRegain;
   private boolean suppressHunger;
   private boolean sticky;
   private int viewDistance;
   private boolean keepSpawnInMemory;
   private GameMode gamemode;
   private Difficulty difficulty;
   private boolean announcePlayerDeath;
   private DataWorld.RespawnLocation respawnLocation;
   private String respawnWorld;
   private String inventoryGroup;
   private String loginMessage;
   private String generatorSettings;
   private long lastAction;
   private World world;
   private boolean removeStructures;

   public DataWorld(XcraftGate instance) {
      this(instance, (String)null, Environment.NORMAL, WorldType.NORMAL, (GeneratorType)null);
   }

   public DataWorld(XcraftGate instance, String worldName) {
      this(instance, worldName, Environment.NORMAL, WorldType.NORMAL, (GeneratorType)null);
   }

   public DataWorld(XcraftGate instance, String worldName, Environment env) {
      this(instance, worldName, env, WorldType.NORMAL, (GeneratorType)null);
   }

   public DataWorld(XcraftGate instance, String worldName, Environment env, WorldType worldType, GeneratorType gen) {
      this.allowWeatherChange = true;
      this.creatureLimit = 0;
      this.border = 0;
      this.setWeather = DataWorld.Weather.SUN;
      this.setTime = 100L;
      this.timeFrozen = false;
      this.suppressHealthRegain = false;
      this.suppressHunger = false;
      this.sticky = false;
      this.viewDistance = 10;
      this.keepSpawnInMemory = false;
      this.announcePlayerDeath = true;
      this.respawnLocation = DataWorld.RespawnLocation.WORLDSPAWN;
      this.respawnWorld = null;
      this.plugin = instance;
      this.allowPvP = Util.castBoolean(this.plugin.serverconfig.getProperty("pvp", "false"));
      this.gamemode = Util.castGameMode(this.plugin.serverconfig.getProperty("gameMode"));
      this.difficulty = Util.castDifficulty(this.plugin.serverconfig.getProperty("difficulty"));
      this.allowAnimals = Util.castBoolean(this.plugin.serverconfig.getProperty("spawn-animals", "true"));
      this.allowMonsters = Util.castBoolean(this.plugin.serverconfig.getProperty("spawn-monsters", "true"));
      this.world = this.plugin.getServer().getWorld(worldName);
      this.name = worldName;
      this.inventoryGroup = "survival";
      this.environment = env;
      this.generator = gen != null ? gen : GeneratorType.DEFAULT;
      this.worldType = worldType;
      this.lastAction = System.currentTimeMillis();
      this.loginMessage = "none";
   }

   public void setRemoveStructures(boolean removeStructures) {
      this.removeStructures = removeStructures;
   }

   public void setGeneratorSettings(String generatorSettings) {
      this.generatorSettings = generatorSettings;
   }

   public void load() {
      this.load((Long)null);
   }

   public void load(Long seed) {
      if (this.world == null) {
         ChunkGenerator thisGen = this.generator.getChunkGenerator(this.generatorSettings);
         WorldCreator creator = new WorldCreator(this.name);
         creator.environment(this.environment);
         creator.type(this.worldType);
         creator.generateStructures(!this.removeStructures);
         if (seed != null) {
            creator.seed(seed);
         }

         if (thisGen != null) {
            creator.generator(thisGen);
         }

         this.world = creator.createWorld();
         this.lastAction = System.currentTimeMillis();
         if (this.timeFrozen) {
            this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
         }

         String var10001 = this.name;
         this.plugin.logInfo("loaded world " + var10001 + " (Environment: " + this.environment.toString() + ", Seed: " + this.world.getSeed() + ", Generator: " + this.generator + ")");
      }
   }

   public boolean unload() {
      if (this.world == null) {
         return true;
      } else if (this.world.getPlayers().size() > 0) {
         return false;
      } else {
         this.plugin.getServer().unloadWorld(this.world, true);
         this.plugin.logInfo("unloaded world " + this.world.getName());
         this.world = null;
         return true;
      }
   }

   public boolean isLoaded() {
      return this.world != null;
   }

   public World getWorld() {
      return this.world;
   }

   public void setWorld(World world) {
      this.world = world;
      if (world != null) {
         this.name = world.getName();
         this.environment = world.getEnvironment();
      }

   }

   public String getName() {
      return this.name;
   }

   public Map<String, Object> toMap() {
      Map<String, Object> values = new HashMap();
      values.put("name", this.name);
      values.put("type", this.environment.toString());
      values.put("worldtype", this.worldType.toString());
      values.put("generator", this.generator.toString());
      values.put("border", this.border);
      values.put("creatureLimit", this.creatureLimit);
      values.put("allowAnimals", this.allowAnimals);
      values.put("allowMonsters", this.allowMonsters);
      values.put("allowPvP", this.allowPvP);
      values.put("allowWeatherChange", this.allowWeatherChange);
      values.put("setWeather", this.setWeather.toString());
      values.put("setTime", this.setTime);
      values.put("timeFrozen", this.timeFrozen);
      values.put("suppressHealthRegain", this.suppressHealthRegain);
      values.put("suppressHunger", this.suppressHunger);
      values.put("sticky", this.sticky);
      values.put("gamemode", this.gamemode.toString());
      values.put("difficulty", this.difficulty.toString());
      values.put("announcePlayerDeath", this.announcePlayerDeath);
      values.put("respawnLocation", this.respawnLocation.toString());
      values.put("respawnWorld", this.respawnWorld);
      values.put("inventorygroup", this.inventoryGroup);
      values.put("loginmessage", this.loginMessage);
      values.put("removeStructures", this.removeStructures);
      if (this.generatorSettings != null) {
         values.put("generatorSettings", this.generatorSettings);
      }

      return values;
   }

   private void resetSpawnFlags() {
      this.world.setSpawnFlags(this.allowMonsters, this.allowAnimals);
   }

   public void checkCreatureLimit() {
      if (this.world != null) {
         if (this.creatureLimit > 0) {
            int alive = this.world.getLivingEntities().size() - this.world.getPlayers().size();
            if (alive >= this.creatureLimit) {
               this.world.setSpawnFlags(false, false);
            } else if ((double)alive <= (double)this.creatureLimit * 0.8D) {
               this.resetSpawnFlags();
            }

         }
      }
   }

   public Boolean checkInactive() {
      if (this.world != null && !this.sticky) {
         if (this.world.getPlayers().size() > 0) {
            this.lastAction = System.currentTimeMillis();
            return false;
         } else {
            return this.lastAction + (long)this.plugin.config.getInt("dynworld.maxInactiveTime", 300) * 1000L < System.currentTimeMillis();
         }
      } else {
         return false;
      }
   }

   public void setWorldTime(long time) {
      long actTime = this.world.getTime();
      actTime -= actTime % 24000L;
      actTime += time + 24000L;
      this.world.setTime(actTime);
   }

   public void resetFrozenTimeIfFrozen() {
      if (this.world != null) {
         if (this.timeFrozen) {
            this.setWorldTime(this.setTime - 100L);
         }
      }
   }

   private void killAllMonsters() {
      if (this.world != null) {
         Iterator var1 = this.world.getLivingEntities().iterator();

         while(var1.hasNext()) {
            LivingEntity entity = (LivingEntity)var1.next();
            if (entity instanceof Monster) {
               entity.remove();
            }
         }

      }
   }

   private void killAllAnimals() {
      if (this.world != null) {
         Iterator var1 = this.world.getLivingEntities().iterator();

         while(var1.hasNext()) {
            LivingEntity entity = (LivingEntity)var1.next();
            if (entity instanceof Animals) {
               entity.remove();
            }
         }

      }
   }

   public void setCreatureLimit(Integer limit) {
      this.creatureLimit = limit != null ? limit : 0;
   }

   public boolean isSticky() {
      return this.sticky;
   }

   public void setSticky(Boolean sticky) {
      this.sticky = sticky != null ? sticky : false;
   }

   public void setAllowAnimals(Boolean allow) {
      this.allowAnimals = allow != null ? allow : true;
      this.setParameters();
      if (!this.allowAnimals) {
         this.killAllAnimals();
      }

   }

   public void setAllowMonsters(Boolean allow) {
      this.allowMonsters = allow != null ? allow : true;
      this.setParameters();
      if (!this.allowMonsters) {
         this.killAllMonsters();
      }

   }

   public boolean isAllowWeatherChange() {
      return this.allowWeatherChange;
   }

   public void setAllowWeatherChange(Boolean allow) {
      this.allowWeatherChange = allow != null ? allow : true;
   }

   public int getBorder() {
      return this.border;
   }

   public void setBorder(Integer border) {
      if (border == null) {
         border = 0;
      }

      this.border = border;

      try {
         this.getWorld().getWorldBorder().setCenter(0.0D, 0.0D);
         if (this.border <= 0 || this.border > 30000000) {
            this.getWorld().getWorldBorder().setSize(6.0E7D);
            return;
         }

         this.getWorld().getWorldBorder().setSize((double)(this.border * 2));
      } catch (NullPointerException var3) {
      }

   }

   public void setAllowPvP(Boolean allow) {
      this.allowPvP = allow != null ? allow : false;
      this.setParameters();
   }

   public void setWeather(DataWorld.Weather weather) {
      boolean backup = this.allowWeatherChange;
      this.allowWeatherChange = true;
      this.setWeather = weather;
      this.setParameters();
      this.allowWeatherChange = backup;
   }

   public void setDayTime(DataWorld.DayTime time) {
      this.setTime = (long)time.id;
      this.setParameters(true);
   }

   public void setDayTime(long time) {
      this.setTime = time;
      this.setParameters(true);
   }

   public boolean isTimeFrozen() {
      return this.timeFrozen;
   }

   public void setTimeFrozen(Boolean frozen) {
      this.timeFrozen = frozen != null ? frozen : false;
      if (this.world != null) {
         this.setTime = this.world.getTime() % 24000L;
         this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, !this.timeFrozen);
      }

   }

   public boolean isSuppressHealthRegain() {
      return this.suppressHealthRegain;
   }

   public void setSuppressHealthRegain(Boolean suppressed) {
      this.suppressHealthRegain = suppressed != null ? suppressed : true;
   }

   public boolean isSuppressHunger() {
      return this.suppressHunger;
   }

   public void setSuppressHunger(Boolean suppressed) {
      this.suppressHunger = suppressed != null ? suppressed : true;
   }

   public void setViewDistance(int distance) {
      this.viewDistance = distance;
      this.setParameters();
   }

   public int getViewDistance() {
      return this.viewDistance;
   }

   public void setKeepSpawnInMemory(boolean keep) {
      this.keepSpawnInMemory = keep;
      this.setParameters();
   }

   public boolean getKeepSpawnInMemory() {
      return this.keepSpawnInMemory;
   }

   public void setGameMode(GameMode gamemode) {
      this.gamemode = gamemode;
      this.setParameters();
   }

   public GameMode getGameMode() {
      return this.gamemode;
   }

   public void setDifficulty(Difficulty difficulty) {
      this.difficulty = difficulty;
      this.setParameters();
   }

   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   public void setAnnouncePlayerDeath(boolean announce) {
      this.announcePlayerDeath = announce;
   }

   public boolean getAnnouncePlayerDeath() {
      return this.announcePlayerDeath;
   }

   public void setInventoryGroup(String groupName) {
      this.inventoryGroup = groupName != null ? groupName : this.name;
   }

   public String getInventoryGroup() {
      return this.inventoryGroup;
   }

   public void setRespawnLocation(DataWorld.RespawnLocation loc) {
      this.respawnLocation = loc != null ? loc : DataWorld.RespawnLocation.WORLDSPAWN;
   }

   public String getRespawnWorldName() {
      return this.respawnWorld;
   }

   public void setRespawnWorldName(String name) {
      this.respawnWorld = name;
   }

   public DataWorld.RespawnLocation getRespawnLocation() {
      return this.respawnLocation;
   }

   public boolean checkBorder(Location location) {
      return this.border > 0 && Math.abs(location.getX()) <= (double)this.border && Math.abs(location.getZ()) <= (double)this.border || this.border == 0;
   }

   public String timeToString(long time) {
      if (time <= 3000L) {
         return "SUNRISE";
      } else if (time <= 9000L) {
         return "NOON";
      } else {
         return time <= 15000L ? "SUNSET" : "MIDNIGHT";
      }
   }

   public String getLoginMessage() {
      return this.loginMessage == null ? "none" : this.loginMessage;
   }

   public void setLoginMessage(String message) {
      this.loginMessage = message;
   }

   public void setParameters() {
      this.setParameters(false);
   }

   public void setParameters(Boolean changeTime) {
      if (this.world != null) {
         this.world.setPVP(this.allowPvP);
         this.world.setSpawnFlags(this.allowMonsters, this.allowAnimals);
         this.world.setStorm(this.setWeather.getId() == DataWorld.Weather.STORM.getId());
         this.world.setDifficulty(this.difficulty);
         if (changeTime) {
            this.setWorldTime(this.setTime);
         }

         this.setCreatureLimit(this.creatureLimit);
      }
   }

   public void sendInfo(CommandSender sender) {
      String var10001 = this.name;
      sender.sendMessage("World: " + var10001 + " (" + (this.generator == GeneratorType.DEFAULT ? this.environment.toString() : this.generator.toString()) + ")" + (this.sticky ? " Sticky!" : ""));
      var10001 = this.world == null ? "world not loaded!" : Util.getLocationString(Util.getSaneLocation(this.world.getSpawnLocation()));
      sender.sendMessage("Spawnlocation: " + var10001 + (this.keepSpawnInMemory ? " (Stays in memory!)" : "") + " (" + this.getRespawnLocation().toString() + (this.getRespawnLocation() == DataWorld.RespawnLocation.WORLD ? ": " + this.getRespawnWorldName() : "") + ")");
      Object var7 = this.world != null ? this.world.getSeed() : "world not loaded!";
      sender.sendMessage("Seed: " + var7);
      var7 = this.world != null ? this.world.getPlayers().size() : "world not loaded!";
      sender.sendMessage("Player count: " + var7);
      var7 = this.border > 0 ? this.border : "none";
      sender.sendMessage("Border: " + var7);
      var10001 = this.allowPvP ? "yes" : "no";
      sender.sendMessage("PvP allowed: " + var10001);
      var10001 = this.allowAnimals ? "yes" : "no";
      sender.sendMessage("Animals/Monsters allowed: " + var10001 + " / " + (this.allowMonsters ? "yes" : "no"));
      var10001 = this.world != null ? this.world.getLivingEntities().size() - this.world.getPlayers().size() + "/" + (this.creatureLimit > 0 ? this.creatureLimit : "unlimited") : "world not loaded!";
      sender.sendMessage("Creature count/limit: " + var10001);
      var10001 = this.suppressHealthRegain ? "yes" : "no";
      sender.sendMessage("Health regaining suppressed: " + var10001);
      var10001 = this.suppressHunger ? "yes" : "no";
      sender.sendMessage("Food bar depletion suppressed: " + var10001);
      var10001 = this.setWeather.toString();
      sender.sendMessage("Weather / changes allowed: " + var10001 + " / " + (this.allowWeatherChange ? "yes" : "no"));
      var10001 = this.world != null ? this.timeToString(this.world.getTime()) : "world not loaded!";
      sender.sendMessage("Current Time / frozen: " + var10001 + " / " + (this.timeFrozen ? "yes" : "no"));
      var10001 = this.inventoryGroup;
      sender.sendMessage("Inventory Group: " + var10001);
      var10001 = this.getGameMode().toString();
      sender.sendMessage("GameMode / Difficulty: " + var10001 + " / " + this.difficulty.toString());
      sender.sendMessage("Announce player deaths: " + (this.announcePlayerDeath ? "Yes" : "No"));
      sender.sendMessage("Login Message: " + this.getLoginMessage());
      if (this.world != null) {
         sender.sendMessage("Gamerules:");
         GameRule[] var2 = GameRule.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GameRule<?> gameRule = var2[var4];
            Object value = this.world.getGameRuleValue(gameRule);
            sender.sendMessage(Objects.equals(this.world.getGameRuleDefault(gameRule), value) ? ChatColor.GRAY + "  " + gameRule.getName() + ": " + value : "  " + gameRule.getName() + ": " + value + " (modified)");
         }
      }

   }

   public static enum Weather {
      SUN(0),
      STORM(1);

      private final int id;
      private static final Map<Integer, DataWorld.Weather> lookup = new HashMap();

      private Weather(int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public static DataWorld.Weather getWeather(int id) {
         return (DataWorld.Weather)lookup.get(id);
      }

      // $FF: synthetic method
      private static DataWorld.Weather[] $values() {
         return new DataWorld.Weather[]{SUN, STORM};
      }

      static {
         DataWorld.Weather[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            DataWorld.Weather env = var0[var2];
            lookup.put(env.getId(), env);
         }

      }
   }

   public static enum RespawnLocation {
      WORLDSPAWN,
      BEDSPAWN,
      WORLD;

      private static final Map<String, DataWorld.RespawnLocation> stringLookup = new HashMap();

      public static DataWorld.RespawnLocation getRespawnLocation(String name) {
         return (DataWorld.RespawnLocation)stringLookup.get(name.toLowerCase());
      }

      // $FF: synthetic method
      private static DataWorld.RespawnLocation[] $values() {
         return new DataWorld.RespawnLocation[]{WORLDSPAWN, BEDSPAWN, WORLD};
      }

      static {
         DataWorld.RespawnLocation[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            DataWorld.RespawnLocation env = var0[var2];
            stringLookup.put(env.name().toLowerCase(), env);
         }

      }
   }

   public static enum DayTime {
      SUNRISE(100),
      NOON(6000),
      SUNSET(12100),
      MIDNIGHT(18000);

      private final int id;
      private static final Map<Integer, DataWorld.DayTime> lookup = new HashMap();

      private DayTime(int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public static DataWorld.DayTime getDayTime(int id) {
         return (DataWorld.DayTime)lookup.get(id);
      }

      // $FF: synthetic method
      private static DataWorld.DayTime[] $values() {
         return new DataWorld.DayTime[]{SUNRISE, NOON, SUNSET, MIDNIGHT};
      }

      static {
         DataWorld.DayTime[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            DataWorld.DayTime env = var0[var2];
            lookup.put(env.getId(), env);
         }

      }
   }
}
