package com.github.maxopoly.Genesis.misc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class RandomSelector <Thing> {

	/**
	 * Randomly picks an element out of a given map, where the value in the map
	 * specifies the chance for its key to be selected. The sum of all values in
	 * the map should be 1.0, otherwise this method may return null because no
	 * element was selected
	 * 
	 * @param map Map containing Objects and their respective chances
	 */
	public Thing pickRandomly(Map<Thing, Double> map) {
		if (map == null) {
			return null;
		}
		Random rng = new Random();
		double val = rng.nextDouble();
		double sum = 0;
		for (Entry<Thing, Double> entry : map.entrySet()) {
			sum += entry.getValue();
			if (sum >= val) {
				return entry.getKey();
			}
		}
		return null;
	}
}
