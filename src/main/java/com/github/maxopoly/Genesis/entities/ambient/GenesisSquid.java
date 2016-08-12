package com.github.maxopoly.Genesis.entities.ambient;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisSquid extends GenesisLivingEntity {

	public GenesisSquid(String uniqueTag, String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects) {
		super(EntityType.SQUID, uniqueTag, customName, drops, effects);
	}
}
