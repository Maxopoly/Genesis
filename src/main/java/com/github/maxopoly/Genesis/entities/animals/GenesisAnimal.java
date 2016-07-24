package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisAnimal extends GenesisLivingEntity {

	private boolean ageLocked;
	private boolean isBaby;

	public GenesisAnimal(EntityType entityType, String uniqueTag,
			String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby) {
		super(entityType, uniqueTag, customName, drops, effects);
		this.ageLocked = ageLocked;
		this.isBaby = isBaby;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Animals animal = (Animals) l;
		animal.setAgeLock(ageLocked);
		if (isBaby) {
			animal.setBaby();
		} else {
			animal.setAdult();
		}
		return l;
	}

	/**
	 * @return Whether this animal will ever become an adult (if it's a baby)
	 */
	public boolean isAgeLocked() {
		return ageLocked;
	}

	/**
	 * @return Whether this animal is spawned as a baby
	 */
	public boolean isBaby() {
		return isBaby;
	}
}
