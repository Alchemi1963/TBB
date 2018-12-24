package com.alchemi.tbb.maps;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class EmptyGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		
		return chunk;
		
	}
	
}
