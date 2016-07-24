package com.github.maxopoly.Genesis.entities;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.Drops;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public abstract class GenesisLivingEntity {

	private EntityType entityType;
	private String customName;
	private Map<List<Drops>, Double> drops;
	private RandomSelector<List<Drops>> selector;

	private Map<EffectCause, List<CombatEffect>> effects;

	private String uniqueTag;

	public GenesisLivingEntity(EntityType entityType, String uniqueTag,
			String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects) {
		this.entityType = entityType;
		this.customName = customName;
		this.effects = effects;
		this.uniqueTag = uniqueTag;
		this.drops = drops;
		this.selector = new RandomSelector<List<Drops>>();
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = (LivingEntity) loc.getWorld().spawnEntity(loc,
				entityType);
		if (customName != null) {
			l.setCustomName(customName);
		}
		return l;
	}

	public void triggerEffects(EffectCause cause, Event e) {
		List<CombatEffect> eff = effects.get(cause);
		if (eff != null) {
			for (CombatEffect ce : eff) {
				ce.trigger(e);
			}
		}
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

	public List<Drops> getRandomDrop() {
		return selector.pickRandomly(drops);
	}
}
