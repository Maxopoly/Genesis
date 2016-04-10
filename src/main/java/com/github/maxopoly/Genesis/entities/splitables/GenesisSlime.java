package com.github.maxopoly.Genesis.entities.splitables;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;

public class GenesisSlime extends GenesisSplitableEntity {

	public GenesisSlime(EntityType type, String customName,
			List<CombatEffect> onHitEffects, List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, int childrenCount,
			int initialSize) {
		super(EntityType.SLIME, customName, onHitEffects, onDeathEffects,
				onSpawnEffects, onGetHitEffects, randomEffects, childrenCount,
				initialSize);
	}

}
