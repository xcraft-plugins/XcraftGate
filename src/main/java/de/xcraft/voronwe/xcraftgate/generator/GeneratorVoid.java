package de.xcraft.voronwe.xcraftgate.generator;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class GeneratorVoid extends ChunkGenerator {
   public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
      ChunkData data = this.createChunkData(world);
      if (x == 0 && z == 0) {
         data.setBlock(0, 64, 0, Material.STONE);
      }

      return data;
   }

   public Location getFixedSpawnLocation(World world, Random random) {
      return new Location(world, 0.0D, 64.0D, 0.0D);
   }

   public boolean isParallelCapable() {
      return true;
   }
}
