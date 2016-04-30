package com.github.maxopoly.Genesis.entities.splitables;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;

public class GenesisMagmaCube extends GenesisSplitableEntity {
	
	public GenesisMagmaCube(String uniqueTag, String customName, Map <EffectCause, List <CombatEffect>> effects, int childrenCount,
			int initialSize, boolean recursiveSplit, boolean onlyDropOnSize1) {
		super(EntityType.MAGMA_CUBE, uniqueTag, customName, effects, childrenCount, initialSize, recursiveSplit, onlyDropOnSize1);
	}
}
