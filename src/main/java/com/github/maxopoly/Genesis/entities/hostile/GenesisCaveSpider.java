package com.github.maxopoly.Genesis.entities.hostile;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public class GenesisCaveSpider extends GenesisLivingEntity {

	public GenesisCaveSpider(String uniqueTag, String customName, Map <EffectCause, List <CombatEffect>> effects) {
		super(EntityType.CAVE_SPIDER, uniqueTag, customName, effects);
	}
}
