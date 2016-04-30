package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;

public class GenesisMushroomCow extends GenesisAnimal {
	public GenesisMushroomCow(String uniqueTag, String customName,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby) {
		super(EntityType.MUSHROOM_COW, uniqueTag, customName, effects,
				ageLocked, isBaby);
	}
}
