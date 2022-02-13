package de.xcraft.voronwe.xcraftgate;

import java.util.List;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;

public class Util {
   public static Integer castInt(Object o) {
      if (o == null) {
         return 0;
      } else if (o instanceof Byte) {
         return Integer.valueOf((Byte)o);
      } else if (o instanceof Integer) {
         return (Integer)o;
      } else if (o instanceof Double) {
         return (int)(double)(Double)o;
      } else if (o instanceof Float) {
         return (int)(float)(Float)o;
      } else if (o instanceof Long) {
         return (int)(long)(Long)o;
      } else {
         try {
            return Integer.parseInt(String.valueOf(o));
         } catch (Exception var2) {
            return 0;
         }
      }
   }

   public static Boolean castBoolean(Object o) {
      if (o == null) {
         return false;
      } else if (o instanceof Boolean) {
         return (Boolean)o;
      } else {
         return o instanceof Number ? ((Number)o).longValue() > 0L : String.valueOf(o).equalsIgnoreCase("true");
      }
   }

   public static Difficulty castDifficulty(Object o) {
      if (o instanceof Integer) {
         Integer intDifficulty = (Integer)o;
         return Difficulty.getByValue(intDifficulty);
      } else {
         try {
            return Difficulty.valueOf(String.valueOf(o));
         } catch (IllegalArgumentException var3) {
            return Difficulty.PEACEFUL;
         }
      }
   }

   public static GameMode castGameMode(Object o) {
      if (o instanceof Integer) {
         Integer intGameMode = (Integer)o;
         return GameMode.getByValue(intGameMode);
      } else {
         try {
            return GameMode.valueOf(String.valueOf(o));
         } catch (IllegalArgumentException var3) {
            return GameMode.SURVIVAL;
         }
      }
   }

   public static String getLocationString(Location location) {
      if (location.getWorld() != null) {
         String var10000 = location.getWorld().getName();
         return var10000 + "," + Math.floor(location.getX()) + "," + Math.floor(location.getY()) + "," + Math.floor(location.getZ());
      } else {
         return null;
      }
   }

   public static Location getSaneLocation(Location loc) {
      double x = Math.floor(loc.getX()) + 0.5D;
      double y = loc.getY();
      double z = Math.floor(loc.getZ()) + 0.5D;
      return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
   }

   public static String joinString(List<String> array, String seperator) {
      return String.join(seperator, array);
   }
}
