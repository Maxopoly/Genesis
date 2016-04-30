package com.github.maxopoly.Genesis.areas;

import org.bukkit.Location;
import org.bukkit.World;

public class CircularArea extends Area{
	
	private Location center;
	private double size;
	
	public CircularArea(World w, Location center, double size) {
		super(w);
	}
	
	public boolean inArea(Location loc) {
		//if its in the same world, it's fine
		return loc.getWorld().getUID().equals(world.getUID()) && geometricallyInArea(loc);
	}
	
	public Location getCenter() {
		return center;
	}
	
	public double getSize() {
		return size;
	}
	
	public boolean geometricallyInArea(Location loc) {
		return center.distanceSquared(loc) <= size;
	}
	
}
