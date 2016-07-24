package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisPig extends GenesisAnimal {

	private boolean hasSaddle;

	public GenesisPig(String uniqueTag, String customName,
			Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby, boolean hasSaddle) {
		super(EntityType.PIG, uniqueTag, customName, drops, effects, ageLocked,
				isBaby);
		this.hasSaddle = hasSaddle;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Pig pig = (Pig) l;
		pig.setSaddle(hasSaddle);
		return l;
	}

	public boolean hasSaddle() {
		return hasSaddle;
	}

}
