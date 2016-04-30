package com.github.maxopoly.Genesis.areas;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public abstract class Area {
	
	protected World world;
	
	public Area(World w) {
		this.world = w;
	}
	
	public abstract boolean inArea(Location loc);
	
	public boolean inArea(Block b) {
		return inArea(b.getLocation());
	}
	
	public World getWorld() {
		return world;
	}
}
