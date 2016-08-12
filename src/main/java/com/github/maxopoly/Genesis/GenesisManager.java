package com.github.maxopoly.Genesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.spawning.SpawnFinder;

public class GenesisManager {

	private Map<UUID, GenesisLivingEntity> entities;
	private Map<String, GenesisLivingEntity> configsByIdentifier;
	private List<SpawnFinder> spawns;

	public GenesisManager() {
		this.entities = new TreeMap<UUID, GenesisLivingEntity>();
		this.configsByIdentifier = new HashMap<String, GenesisLivingEntity>();
		this.spawns = new ArrayList<SpawnFinder>();
	}

	public GenesisLivingEntity getEntityConfig(UUID uuid) {
		return entities.get(uuid);
	}

	public GenesisLivingEntity getEntityConfig(LivingEntity e) {
		return getEntityConfig(e.getUniqueId());
	}

	public GenesisLivingEntity getEntityConfig(String identifier) {
		return configsByIdentifier.get(identifier);
	}

	public void registerEntityConfig(GenesisLivingEntity gle) {
		configsByIdentifier.put(gle.getUniqueTag(), gle);
	}

	public List<SpawnFinder> getSpawnFinders() {
		return spawns;
	}

	public void registerSpawnFinder(SpawnFinder sf) {
		this.spawns.add(sf);
	}

}
