package com.github.maxopoly.Genesis.misc;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class Drops {

	private ItemStack item;
	private int lowestAmount;
	private int highestAmount;
	private static Random rng;

	public Drops(ItemStack item, int lowestAmount, int highestAmount) {
		this.item = item;
		if (lowestAmount > highestAmount) {
			throw new IllegalArgumentException("Lower limit " + lowestAmount + " can't be higher than higher limit "
					+ highestAmount);
		}
		this.lowestAmount = lowestAmount;
		this.highestAmount = highestAmount;
		if (Drops.rng == null) {
			Drops.rng = new Random();
		}
	}

	public ItemStack getRandomDrop() {
		int amount = rng.nextInt(highestAmount - lowestAmount + 1) + lowestAmount;
		ItemStack is = item.clone();
		is.setAmount(amount);
		return is;
	}

}
