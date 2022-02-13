package de.xcraft.voronwe.xcraftgate;

import de.xcraft.voronwe.xcraftgate.generator.GeneratorType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.yaml.snakeyaml.Yaml;

public class SetWorld implements Iterable<DataWorld> {
   private final XcraftGate plugin;
   private final Map<String, DataWorld> worlds = new HashMap();

   public SetWorld(XcraftGate plugin) {
      this.plugin = plugin;
   }

   public void load() {
      File configFile = this.plugin.getConfigFile("worlds.yml");
      int counter = 0;

      try {
         Yaml yaml = new Yaml();
         Map<String, Object> worldsYaml = (Map)yaml.load(new FileInputStream(configFile));
         if (worldsYaml == null) {
            this.plugin.logInfo("empty worlds.yml - initializing");
            return;
         }

         for(Iterator var6 = worldsYaml.entrySet().iterator(); var6.hasNext(); ++counter) {
            Entry<String, Object> thisWorld = (Entry)var6.next();
            String worldName = (String)thisWorld.getKey();
            Map<String, Object> worldData = (Map)thisWorld.getValue();
            Environment env = null;
            GeneratorType gen = null;
            WorldType worldType = null;
            String checkEnv = (String)worldData.get("type");
            Environment[] var14 = Environment.values();
            int var15 = var14.length;

            int var16;
            for(var16 = 0; var16 < var15; ++var16) {
               Environment thisEnv = var14[var16];
               if (thisEnv.toString().equalsIgnoreCase(checkEnv)) {
                  env = thisEnv;
               }
            }

            WorldType[] var21 = WorldType.values();
            var15 = var21.length;

            for(var16 = 0; var16 < var15; ++var16) {
               WorldType thisEnv = var21[var16];
               if (thisEnv.toString().equalsIgnoreCase(checkEnv)) {
                  worldType = thisEnv;
               }
            }

            if (env == null) {
               env = Environment.NORMAL;
            }

            if (worldType == null) {
               worldType = WorldType.NORMAL;
            }

            String checkGen = (String)worldData.get("generator");
            GeneratorType[] var23 = GeneratorType.values();
            var16 = var23.length;

            int var26;
            for(var26 = 0; var26 < var16; ++var26) {
               GeneratorType thisGen = var23[var26];
               if (thisGen.toString().equalsIgnoreCase(checkGen)) {
                  gen = thisGen;
               }
            }

            DataWorld newWorld = new DataWorld(this.plugin, worldName, env, worldType, gen);
            newWorld.setBorder(Util.castInt(worldData.get("border")));
            newWorld.setAllowPvP(Util.castBoolean(worldData.get("allowPvP")));
            newWorld.setAllowAnimals(Util.castBoolean(worldData.get("allowAnimals")));
            newWorld.setAllowMonsters(Util.castBoolean(worldData.get("allowMonsters")));
            newWorld.setCreatureLimit(Util.castInt(worldData.get("creatureLimit")));
            newWorld.setAllowWeatherChange(Util.castBoolean(worldData.get("allowWeatherChange")));
            newWorld.setTimeFrozen(Util.castBoolean(worldData.get("timeFrozen")));
            newWorld.setDayTime((long)Util.castInt(worldData.get("setTime")));
            newWorld.setSuppressHealthRegain(Util.castBoolean(worldData.get("suppressHealthRegain")));
            newWorld.setSuppressHunger(Util.castBoolean(worldData.get("suppressHunger")));
            newWorld.setSticky(Util.castBoolean(worldData.get("sticky")));
            newWorld.setAnnouncePlayerDeath(Util.castBoolean(worldData.get("announcePlayerDeath")));
            newWorld.setDifficulty(Util.castDifficulty(worldData.get("difficulty")));
            newWorld.setGameMode(Util.castGameMode(worldData.get("gamemode")));
            newWorld.setRespawnWorldName((String)worldData.get("respawnWorld"));
            newWorld.setInventoryGroup((String)worldData.get("inventorygroup"));
            newWorld.setLoginMessage((String)worldData.get("loginmessage"));
            newWorld.setRemoveStructures(Util.castBoolean(worldData.get("removeStructures")));
            newWorld.setGeneratorSettings((String)worldData.get("generatorSettings"));
            this.worlds.put(worldName, newWorld);
            String weather = (String)worldData.get("setWeather");
            DataWorld.Weather[] var27 = DataWorld.Weather.values();
            var26 = var27.length;

            for(int var29 = 0; var29 < var26; ++var29) {
               DataWorld.Weather thisWeather = var27[var29];
               if (thisWeather.toString().equalsIgnoreCase(weather)) {
                  newWorld.setWeather(thisWeather);
               }
            }

            String respawn = (String)worldData.get("respawnLocation");
            newWorld.setRespawnLocation(DataWorld.RespawnLocation.getRespawnLocation(respawn));
         }
      } catch (Exception var20) {
         var20.printStackTrace();
      }

      this.plugin.logInfo("loaded " + counter + " world configurations");
   }

   public void save() {
      File configFile = this.plugin.getConfigFile("worlds.yml");
      Map<String, Object> toDump = new HashMap();
      Iterator var3 = this.worlds.values().iterator();

      while(var3.hasNext()) {
         DataWorld thisWorld = (DataWorld)var3.next();
         toDump.put(thisWorld.getName(), thisWorld.toMap());
      }

      Yaml yaml = new Yaml();
      String dump = yaml.dump(toDump);

      try {
         FileOutputStream fh = new FileOutputStream(configFile);
         (new PrintStream(fh)).println(dump);
         fh.flush();
         fh.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void onWorldLoad(World world) {
      if (this.worlds.get(world.getName()) != null) {
         this.plugin.logInfo("World '" + world.getName() + "' loading. Applying config.");
         this.get(world).setWorld(world);
         this.get(world).setParameters();
      } else {
         this.plugin.logInfo("World '" + world.getName() + "' detected. Adding to config.");
         DataWorld newWorld = new DataWorld(this.plugin, world.getName(), world.getEnvironment());
         this.add(newWorld);
         this.save();
      }

      this.plugin.getGates().onWorldLoad(this.get(world));
   }

   public void add(DataWorld world) {
      this.worlds.put(world.getName(), world);
      this.save();
   }

   public void remove(String worldName) {
      this.worlds.remove(worldName);
      this.save();
   }

   public DataWorld get(World world) {
      return this.get(world.getName());
   }

   public DataWorld get(Location location) {
      if (location == null) {
         return null;
      } else {
         World world = location.getWorld();
         return world == null ? null : this.get(world);
      }
   }

   public DataWorld get(String name) {
      return (DataWorld)this.worlds.get(name);
   }

   public Collection<String> names() {
      return this.worlds.keySet();
   }

   public Iterator<DataWorld> iterator() {
      return this.worlds.values().iterator();
   }

   public Set<String> existingInventoryGroups() {
      return (Set)this.worlds.values().stream().map(DataWorld::getInventoryGroup).collect(Collectors.toSet());
   }

   public List<String> loaded() {
      return (List)this.worlds.values().stream().filter(DataWorld::isLoaded).map(DataWorld::getName).collect(Collectors.toList());
   }
}
