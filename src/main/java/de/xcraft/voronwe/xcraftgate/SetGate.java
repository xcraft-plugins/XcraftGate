package de.xcraft.voronwe.xcraftgate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.yaml.snakeyaml.Yaml;

public class SetGate implements Iterable<DataGate> {
   private final XcraftGate plugin;
   private final Map<String, DataGate> gates = new HashMap();
   private final Map<String, String> gateLocations = new HashMap();

   public SetGate(XcraftGate plugin) {
      this.plugin = plugin;
   }

   public void load() {
      File configFile = this.plugin.getConfigFile("gates.yml");
      int counter = 0;

      try {
         Yaml yaml = new Yaml();
         this.plugin.logInfo("accessing gates");
         Map<String, Object> gatesYaml = (Map)yaml.load(new FileInputStream(configFile));
         if (gatesYaml == null) {
            this.plugin.logInfo("empty gates.yml - initializing");
            return;
         }

         this.plugin.logInfo("interpreting gates");

         Iterator var5;
         Entry thisGate;
         String gateName;
         Map gateData;
         DataGate thisTarget;
         for(var5 = gatesYaml.entrySet().iterator(); var5.hasNext(); ++counter) {
            thisGate = (Entry)var5.next();
            gateName = (String)thisGate.getKey();
            gateData = (Map)thisGate.getValue();
            thisTarget = new DataGate(this.plugin, gateName);
            thisTarget.setLocation((String)gateData.get("world"), (Double)gateData.get("locX"), (Double)gateData.get("locY"), (Double)gateData.get("locZ"), ((Double)gateData.get("locYaw")).floatValue(), ((Double)gateData.get("locP")).floatValue());
            thisTarget.setToll((Double)gateData.get("toll"));
            thisTarget.setDenySilent((Boolean)gateData.get("denysilent"));
            this.gates.put(thisTarget.getName(), thisTarget);
         }

         var5 = gatesYaml.entrySet().iterator();

         while(var5.hasNext()) {
            thisGate = (Entry)var5.next();
            gateName = (String)thisGate.getKey();
            gateData = (Map)thisGate.getValue();
            if (gateData.get("target") != null) {
               thisTarget = this.get((String)gateData.get("target"));
               if (thisTarget == null) {
                  this.plugin.logWarning("ignored invalid destination for gate " + gateName);
               } else {
                  this.get(gateName).linkTo(thisTarget, false);
               }
            }
         }

         this.gates.values().stream().map(DataGate::getName).forEach(this::resetSuperPermission);
         this.gates.values().forEach((gate) -> {
            Location location = gate.getLocation();
            if (location != null) {
               this.gateLocations.put(Util.getLocationString(location), gate.getName());
            }

         });
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      this.plugin.logInfo("loaded " + counter + " gates");
   }

   public void save() {
      File configFile = this.plugin.getConfigFile("gates.yml");
      Map<String, Object> toDump = new HashMap();
      Iterator var3 = this.gates.values().iterator();

      while(var3.hasNext()) {
         DataGate thisGate = (DataGate)var3.next();
         toDump.put(thisGate.getName(), thisGate.toMap());
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

   public void reload() {
      this.gates.clear();
      this.gateLocations.clear();
      this.load();
      Iterator var1 = this.gates.values().iterator();

      while(var1.hasNext()) {
         DataGate thisGate = (DataGate)var1.next();
         if (this.plugin.getServer().getWorld(thisGate.getWorldName()) != null) {
            this.gateLocations.put(Util.getLocationString(thisGate.getLocation()), thisGate.getName());
         }
      }

   }

   public void addAndSave(DataGate gate, boolean save) {
      this.gates.put(gate.getName(), gate);
      if (gate.getLocation() != null) {
         this.gateLocations.put(Util.getLocationString(gate.getLocation()), gate.getName());
      }

      this.resetSuperPermission(gate.getName());
      if (save) {
         this.save();
      }

   }

   public void remove(DataGate gate) {
      this.gates.remove(gate.getName());
      if (this.plugin.getWorlds().get(gate.getWorldName()).isLoaded()) {
         this.gateLocations.remove(Util.getLocationString(gate.getLocation()));
      }

      this.save();
   }

   public boolean has(String name) {
      return this.gates.containsKey(name);
   }

   public DataGate get(String gateName) {
      return (DataGate)this.gates.get(gateName);
   }

   public DataGate getByLocation(Location loc) {
      String gateName = (String)this.gateLocations.get(Util.getLocationString(loc));
      return this.get(gateName);
   }

   public void resetSuperPermission(String gatePerm) {
      org.bukkit.plugin.PluginManager pm = this.plugin.getServer().getPluginManager();
      gatePerm = "XcraftGate.use." + gatePerm;
      if (pm.getPermission(gatePerm) == null) {
         pm.addPermission(new Permission(gatePerm, PermissionDefault.TRUE));
      }

      Permission superPerm = pm.getPermission("XcraftGate.use.*");
      if (superPerm != null) {
         if (superPerm.getChildren().containsKey(gatePerm)) {
            return;
         }

         pm.removePermission("xcraftgate.use.*");
      }

      String descr = "Permission to use all gates";
      Map<String, Boolean> children = new HashMap();
      Iterator var6 = this.gates.keySet().iterator();

      while(var6.hasNext()) {
         String name = (String)var6.next();
         children.put("XcraftGate.use." + name, true);
      }

      superPerm = new Permission("XcraftGate.use.*", descr, superPerm != null ? superPerm.getDefault() : PermissionDefault.TRUE, children);
      pm.addPermission(superPerm);
   }

   public void onWorldLoad(DataWorld world) {
      int gateCounter = 0;
      Iterator var3 = this.gates.values().iterator();

      while(var3.hasNext()) {
         DataGate thisGate = (DataGate)var3.next();
         if (thisGate.getWorldName().equalsIgnoreCase(world.getName())) {
            this.gateLocations.put(Util.getLocationString(thisGate.getLocation()), thisGate.getName());
            ++gateCounter;
         }
      }

      this.plugin.logInfo("loaded " + gateCounter + " gates for world '" + world.getName() + "'");
   }

   public void onWorldUnload(World world) {
      this.plugin.getWorlds().get(world);
   }

   public void onWorldUnload(DataWorld world) {
      Iterator var2 = this.gates.values().iterator();

      while(var2.hasNext()) {
         DataGate thisGate = (DataGate)var2.next();
         if (thisGate.getWorldName().equalsIgnoreCase(world.getName())) {
            this.gateLocations.remove(Util.getLocationString(thisGate.getLocation()));
         }
      }

   }

   public int size() {
      return this.gates.size();
   }

   public Object[] toArray() {
      return this.gates.values().toArray();
   }

   public Object[] namesArray() {
      return this.gates.keySet().toArray();
   }

   public Collection<String> names() {
      return this.gates.keySet();
   }

   public Iterator<DataGate> iterator() {
      return this.gates.values().iterator();
   }
}
