package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisMushroomCow extends GenesisAnimal {
	public GenesisMushroomCow(String uniqueTag, String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked, boolean isBaby) {
		super(EntityType.MUSHROOM_COW, uniqueTag, customName, drops, effects, ageLocked, isBaby);
	}
}
