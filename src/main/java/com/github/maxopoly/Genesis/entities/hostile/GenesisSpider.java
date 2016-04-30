package com.github.maxopoly.Genesis.entities.hostile;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public class GenesisSpider extends GenesisLivingEntity {
	
	public GenesisSpider(String uniqueTag, String customName, Map <EffectCause, List <CombatEffect>> effects) {
		super(EntityType.SPIDER, uniqueTag, customName, effects);
	}

}
