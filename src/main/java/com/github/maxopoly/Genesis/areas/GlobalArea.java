package com.github.maxopoly.Genesis.areas;

import org.bukkit.Location;
import org.bukkit.World;

public class GlobalArea extends Area{
	
	public GlobalArea(World w) {
		super(w);
	}
	
	public boolean inArea(Location loc) {
		//if its in the same world, it's fine
		return loc.getWorld().getUID().equals(world.getUID());
	}

}
