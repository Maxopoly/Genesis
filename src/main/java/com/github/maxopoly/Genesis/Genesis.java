package com.github.maxopoly.Genesis;

import vg.civcraft.mc.civmodcore.ACivMod;

public class Genesis extends ACivMod {

	private static Genesis instance;
	private static GenesisManager manager;

	public void onEnable() {
		instance = this;
		manager = new GenesisManager();
		ConfigParser cp = new ConfigParser(this);
		cp.parse();
	}

	public void onDisable() {

	}

	public String getPluginName() {
		return "Genesis";
	}

	public static Genesis getInstance() {
		return instance;
	}

	public static GenesisManager getManager() {
		return manager;
	}
}
