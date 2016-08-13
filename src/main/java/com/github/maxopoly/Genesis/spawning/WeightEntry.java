package com.github.maxopoly.Genesis.spawning;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

/**
 * All the values stored in an instance of this class will be gradually reduced
 * over time, each spawningWeightTimer (which is defined for each entity type),
 * the value will be reduced by 1. This update will not be executed instantly,
 * instead it will be done the next time a value for this chunk is request, this
 * way a ton of pointless constant attempted updates are avoided
 *
 */
public class WeightEntry {

	private long timeStamp;
	private Map<GenesisLivingEntity, Integer> weights;
	private static long minimumUpdateTime;

	public WeightEntry() {
		this.weights = new TreeMap<GenesisLivingEntity, Integer>();
		this.timeStamp = System.currentTimeMillis();
	}

	public WeightEntry(Map<GenesisLivingEntity, Integer> weights) {
		this.weights = weights;
		this.timeStamp = System.currentTimeMillis();
	}

	/**
	 * Gets the current updated weight in this chunk for the given entity
	 * 
	 * @param type
	 *            Entity to get weight for
	 * @return current updated weight
	 */
	public int getWeight(GenesisLivingEntity type) {
		update();
		Integer entry = weights.get(type);
		if (entry == null) {
			return 0;
		}
		return entry;
	}

	/**
	 * Increases the weight for this chunk for the given entity by the given
	 * amount
	 * 
	 * @param type
	 *            Entity type to increase weight for
	 * @param amount
	 *            Amount to increase by
	 */
	public void increaseWeight(GenesisLivingEntity type, int amount) {
		int currValue;
		Integer entry = weights.get(type);
		if (entry == null) {
			currValue = 0;
		} else {
			currValue = entry;
		}
		currValue += amount;
		weights.put(type, currValue);
	}

	/**
	 * Updates the weight for all entities according to their time spawningWeightTimer
	 */
	public void update() {
		long current = System.currentTimeMillis();
		if ((current - timeStamp) < minimumUpdateTime) {
			return;
		}
		for (Entry<GenesisLivingEntity, Integer> entry : weights.entrySet()) {
			int weightToChange = (int) ((current - timeStamp) / entry.getKey().getSpawningWeightTimer());
			entry.setValue(Math.max(entry.getValue() - weightToChange, 0));
		}
		timeStamp = current;
	}
}
