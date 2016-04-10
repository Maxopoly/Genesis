package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;

public class GenesisSheep extends GenesisAnimal {
	
	private boolean isSheared;
	
	public GenesisSheep(String customName, List<CombatEffect> onHitEffects,
			List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, boolean ageLocked,
			boolean isBaby, boolean isSheared) {
		super(EntityType.SHEEP, customName, onHitEffects, onDeathEffects,
				onSpawnEffects, onGetHitEffects, randomEffects, ageLocked,
				isBaby);
		this.isSheared = isSheared;
	}
	
	public LivingEntity spawnAt(Location loc) {
		Sheep sheep = (Sheep) super.spawnAt(loc);
		sheep.setSheared(isSheared);
		return sheep;
	}
	
	/**
	 * @return Whether the entity spawned is sheared
	 */
	public boolean isSheared() {
		return isSheared;
	}
}
