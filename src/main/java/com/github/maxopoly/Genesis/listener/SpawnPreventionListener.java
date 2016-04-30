package com.github.maxopoly.Genesis.listener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.maxopoly.Genesis.Genesis;

public class SpawnPreventionListener implements Listener {
	
	private Set <SpawnReason> disallowedSpawns;
	
	public SpawnPreventionListener(Set <SpawnReason> disallowedSpawns) {
		this.disallowedSpawns = disallowedSpawns;
		Bukkit.getPluginManager().registerEvents(this, Genesis.instance());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void entitySpawn(CreatureSpawnEvent e) {
		if (disallowedSpawns.contains(e.getSpawnReason())) {
			e.setCancelled(true);
		}
	}
}
