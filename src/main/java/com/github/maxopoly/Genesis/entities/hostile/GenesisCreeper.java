package com.github.maxopoly.Genesis.entities.hostile;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public class GenesisCreeper extends GenesisLivingEntity {

	private boolean isPowered;

	public GenesisCreeper(String customName, List<CombatEffect> onHitEffects,
			List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, boolean isPowered) {
		super(EntityType.CREEPER, customName, onHitEffects, onDeathEffects,
				onSpawnEffects, onGetHitEffects, randomEffects);
	}

	public LivingEntity spawnAt(Location loc) {
		Creeper creeper = (Creeper) super.spawnAt(loc);
		creeper.setPowered(isPowered);
		return creeper;
	}

	/**
	 * @return Whether this creeper is powered
	 */
	public boolean isPowered() {
		return isPowered;
	}
}
