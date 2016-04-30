package com.github.maxopoly.Genesis.areas;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class BiomeArea extends Area {
	
	private List <Biome> biomes;

	public BiomeArea(World w, List <Biome> biomes) {
		super(w);
		this.biomes = biomes;
	}
	
	public boolean inArea(Location loc) {
		return loc.getWorld().getUID().equals(world.getUID()) && biomes.contains(loc.getBlock().getBiome());
	}
	
	public List <Biome> getBiomes() {
		return biomes;
	}
}
