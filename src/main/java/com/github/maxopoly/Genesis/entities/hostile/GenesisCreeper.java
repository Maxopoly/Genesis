package com.github.maxopoly.Genesis.entities.hostile;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisCreeper extends GenesisLivingEntity {

	private boolean isPowered;

	public GenesisCreeper(String uniqueTag, String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean isPowered) {
		super(EntityType.CREEPER, uniqueTag, customName, drops, effects);
		this.isPowered = isPowered;
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
