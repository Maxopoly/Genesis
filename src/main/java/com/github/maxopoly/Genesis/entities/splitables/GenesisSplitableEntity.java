package com.github.maxopoly.Genesis.entities.splitables;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public abstract class GenesisSplitableEntity extends GenesisLivingEntity {

	private int childrenCount;
	private int initialSize;

	public GenesisSplitableEntity(EntityType type, String customName,
			List<CombatEffect> onHitEffects, List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, int childrenCount,
			int initialSize) {
		super(type, customName, onHitEffects, onDeathEffects, onSpawnEffects,
				onGetHitEffects, randomEffects);
		this.childrenCount = childrenCount;
		this.initialSize = initialSize;
	}
	
	public LivingEntity spawnAt(Location loc) {
		Slime slime = (Slime) super.spawnAt(loc);
		slime.setSize(initialSize);
		return slime;
	}

	

	public int getChildrenCount() {
		return childrenCount;
	}

	public int getInitalSize() {
		return initialSize;
	}
}
