package com.github.maxopoly.Genesis;

import vg.civcraft.mc.civmodcore.ACivMod;

public class Genesis extends ACivMod {
	
	private static Genesis instance;
	
	public void onEnable() {
		instance = this;
	}
	
	public void onDisable() {
		
	}
	
	public String getPluginName() {
		return "Genesis";
	}
	
	public static Genesis instance() {
		return instance;
	}
}
