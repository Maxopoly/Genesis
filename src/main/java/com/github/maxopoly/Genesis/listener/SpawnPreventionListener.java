package com.github.maxopoly.Genesis.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.maxopoly.Genesis.Genesis;

public class SpawnPreventionListener implements Listener {
	
	private boolean cancelNaturalSpawns;
	
	public SpawnPreventionListener(boolean cancelNaturalSpawns) {
		this.cancelNaturalSpawns = cancelNaturalSpawns;
		Bukkit.getPluginManager().registerEvents(this, Genesis.getInstance());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void entitySpawn(CreatureSpawnEvent e) {
		if (cancelNaturalSpawns && e.getSpawnReason() == SpawnReason.NATURAL) {
			e.setCancelled(true);
		}
	}
}
