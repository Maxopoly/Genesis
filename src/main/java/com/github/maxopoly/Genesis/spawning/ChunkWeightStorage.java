package com.github.maxopoly.Genesis.spawning;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public class ChunkWeightStorage {

	private Map<UUID, Map<Long, WeightEntry>> weights;

	public ChunkWeightStorage() {
		this.weights = new TreeMap<UUID, Map<Long,WeightEntry>>();
	}
	
	public void increaseWeightForChunk(UUID world, int chunkX, int chunkZ, GenesisLivingEntity entity, int amount) {
		Map <Long, WeightEntry> worldMap = weights.get(world);
		if (worldMap == null) {
			worldMap = new TreeMap<Long, WeightEntry>();
			weights.put(world, worldMap);
		}
		long id = generateChunkIdentifier(chunkX, chunkZ);
		WeightEntry entry = worldMap.get(id);
		if (entry == null) {
			entry = new WeightEntry();
			worldMap.put(id, entry);
		}
		entry.increaseWeight(entity, amount);
	}

	public int getWeightForChunk(UUID world, int chunkX, int chunkZ, GenesisLivingEntity entity) {
		Map <Long, WeightEntry> worldMap = weights.get(world);
		if (worldMap == null) {
			return 0;
		}
		long id = generateChunkIdentifier(chunkX, chunkZ);
		WeightEntry entry = worldMap.get(id);
		if (entry == null) {
			return 0;
		}
		return entry.getWeight(entity);
	}

	public static long generateChunkIdentifier(int chunkX, int chunkZ) {
		long res = chunkX;
		res = res << 32;
		res += (long) chunkZ;
		return res;
	}

}
