package com.github.maxopoly.Genesis.entities.hostile;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisBlaze extends GenesisLivingEntity {

	public GenesisBlaze(String uniqueTag, String customName,
			Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects) {
		super(EntityType.BLAZE, uniqueTag, customName, drops, effects);
	}
}
