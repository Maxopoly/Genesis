package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;

public class GenesisChicken extends GenesisAnimal {

	public GenesisChicken(String uniqueTag, String customName,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby) {
		super(EntityType.CHICKEN, uniqueTag, customName, effects, ageLocked, isBaby);
	}

}
