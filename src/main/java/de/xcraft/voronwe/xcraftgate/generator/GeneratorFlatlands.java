package de.xcraft.voronwe.xcraftgate.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class GeneratorFlatlands extends ChunkGenerator {
   private final GeneratorFlatlands.Layer[] layers;

   public GeneratorFlatlands(String generatorSettings) {
      if (generatorSettings == null) {
         this.layers = new GeneratorFlatlands.Layer[]{new GeneratorFlatlands.Layer(0, 54, Material.STONE), new GeneratorFlatlands.Layer(54, 64, Material.DIRT), new GeneratorFlatlands.Layer(64, 65, Material.GRASS_BLOCK)};
      } else {
         List<GeneratorFlatlands.Layer> layerList = new ArrayList();
         int previousEnd = 0;
         String[] var4 = generatorSettings.split(",");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String layer = var4[var6];
            if (!layer.isBlank()) {
               String[] split = layer.split(":");
               Material material = Material.getMaterial(split[0].toUpperCase());
               int height = split.length > 1 ? Integer.parseInt(split[1]) : 1;
               layerList.add(new GeneratorFlatlands.Layer(previousEnd, previousEnd + height, material == null ? Material.BEDROCK : material));
               previousEnd += height;
            }
         }

         this.layers = (GeneratorFlatlands.Layer[])layerList.toArray(new GeneratorFlatlands.Layer[0]);
      }

   }

   public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
      ChunkData data = this.createChunkData(world);
      GeneratorFlatlands.Layer[] var7 = this.layers;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         GeneratorFlatlands.Layer layer = var7[var9];
         data.setRegion(0, layer.from, 0, 16, layer.to, 16, layer.material);
      }

      return data;
   }

   public Location getFixedSpawnLocation(World world, Random random) {
      int x = random.nextInt(200) - 100;
      int z = random.nextInt(200) - 100;
      int y = world.getHighestBlockYAt(x, z);
      return new Location(world, (double)x, (double)y, (double)z);
   }

   public boolean isParallelCapable() {
      return true;
   }

   private static class Layer {
      int from;
      int to;
      Material material;

      public Layer(int from, int to, Material material) {
         this.from = from;
         this.to = to;
         this.material = material;
      }
   }
}
