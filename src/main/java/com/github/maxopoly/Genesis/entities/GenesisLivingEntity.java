package com.github.maxopoly.Genesis.entities;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.misc.NBTTagEditor;

public abstract class GenesisLivingEntity {

	private EntityType entityType;
	private String customName;

	private List<CombatEffect> onHitEffects;
	private List<CombatEffect> onDeathEffects;
	private List<CombatEffect> onSpawnEffects;
	private List<CombatEffect> onGetHitEffects;
	private Map<CombatEffect, Long> randomEffects;

	private String uniqueTag;

	private final String NBTTagKey = "genesisIdentifier";

	public GenesisLivingEntity(EntityType entityType, String customName,
			List<CombatEffect> onHitEffects, List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects) {
		this.entityType = entityType;
		this.customName = customName;
		this.onDeathEffects = onDeathEffects;
		this.onHitEffects = onHitEffects;
		this.onSpawnEffects = onSpawnEffects;
		this.onGetHitEffects = onGetHitEffects;
		this.randomEffects = randomEffects;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = (LivingEntity) loc.getWorld().spawnCreature(loc,
				entityType);
		if (customName != null) {
			l.setCustomName(customName);
		}
		NBTTagEditor.addTag(l, NBTTagKey, uniqueTag);
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

	/**
	 * @return Effects possibly applied when this entity hits another one
	 */
	public List<CombatEffect> getOnHitEffects() {
		return onHitEffects;
	}

	/**
	 * @return Effects possibly applied when this entity dies to a player
	 */
	public List<CombatEffect> getOnDeathEffects() {
		return onDeathEffects;
	}

	/**
	 * @return Effects possibly applied when this gets hit
	 */
	public List<CombatEffect> getOnGetHitEffects() {
		return onGetHitEffects;
	}

	/**
	 * @return Effects possibly applied when this entity spawns
	 */
	public List<CombatEffect> getOnSpawnEffects() {
		return onSpawnEffects;
	}

	/**
	 * @return Effects randomly applied for this entity, key is the intervall
	 *         and value the effect
	 */
	public Map<CombatEffect, Long> getRandomEffects() {
		return randomEffects;
	}
}
