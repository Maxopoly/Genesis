package com.github.maxopoly.Genesis.entities;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;

public abstract class GenesisLivingEntity {

	private EntityType entityType;
	private String customName;

	private Map <EffectCause, List <CombatEffect>> effects;

	private String uniqueTag;

	public GenesisLivingEntity(EntityType entityType, String uniqueTag, String customName,
			Map <EffectCause, List <CombatEffect>> effects) {
		this.entityType = entityType;
		this.customName = customName;
		this.effects = effects;
		this.uniqueTag = uniqueTag;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = (LivingEntity) loc.getWorld().spawnEntity(loc, entityType);
		if (customName != null) {
			l.setCustomName(customName);
		}
		return l;
	}

	/**
	 * @return Customname of this entity or null if none is specified
	 */
	public String getCustomName() {
		return customName;
	}

	/**
	 * @return Tag that will be added to mobs of this type
	 */
	public String getUniqueTag() {
		return uniqueTag;
	}

	/**
	 * @return Type of this entity
	 */
	public EntityType getType() {
		return entityType;
	}
}
