package com.github.maxopoly.Genesis.entities.ambient;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisBat extends GenesisLivingEntity {
	public GenesisBat(String uniqueTag, String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects) {
		super(EntityType.BAT, uniqueTag, customName, drops, effects);
	}
}
