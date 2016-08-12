package com.github.maxopoly.Genesis.entities.hostile;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisGuardian extends GenesisLivingEntity {

	private boolean isElder;

	public GenesisGuardian(String uniqueTag, String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean isElder) {
		super(EntityType.CREEPER, uniqueTag, customName, drops, effects);
		this.isElder = isElder;
	}

	public LivingEntity spawnAt(Location loc) {
		Guardian guardian = (Guardian) super.spawnAt(loc);
		guardian.setElder(isElder);
		return guardian;
	}

	/**
	 * @return Whether this guardian is an elder
	 */
	public boolean isElder() {
		return isElder;
	}
}
