package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;

public class GenesisMushroomCow extends GenesisAnimal {
	public GenesisMushroomCow(String customName,
			List<CombatEffect> onHitEffects, List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, boolean ageLocked,
			boolean isBaby) {
		super(EntityType.MUSHROOM_COW, customName, onHitEffects,
				onDeathEffects, onSpawnEffects, onGetHitEffects, randomEffects,
				ageLocked, isBaby);
	}
}
