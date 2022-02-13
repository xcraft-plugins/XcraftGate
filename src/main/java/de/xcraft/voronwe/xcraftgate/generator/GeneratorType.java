package de.xcraft.voronwe.xcraftgate.generator;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.generator.ChunkGenerator;

public enum GeneratorType {
   DEFAULT(0),
   FLATLANDS(1),
   VOID(2),
   GRID(3);

   private final int id;
   private static final Map<Integer, GeneratorType> lookup = new HashMap();

   private GeneratorType(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public ChunkGenerator getChunkGenerator(String generatorSettings) {
      Object var10000;
      switch(this) {
      case FLATLANDS:
         var10000 = new GeneratorFlatlands(generatorSettings);
         break;
      case VOID:
         var10000 = new GeneratorVoid();
         break;
      default:
         var10000 = null;
      }

      return (ChunkGenerator)var10000;
   }

   // $FF: synthetic method
   private static GeneratorType[] $values() {
      return new GeneratorType[]{DEFAULT, FLATLANDS, VOID, GRID};
   }
}
