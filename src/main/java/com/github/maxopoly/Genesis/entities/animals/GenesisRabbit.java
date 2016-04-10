package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public class GenesisRabbit extends GenesisAnimal {

	private static RandomSelector<Rabbit.Type> typeSelector;

	private Map<Rabbit.Type, Double> types;

	public GenesisRabbit(String customName, List<CombatEffect> onHitEffects,
			List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, boolean ageLocked,
			boolean isBaby, Map<Rabbit.Type, Double> types) {
		super(EntityType.RABBIT, customName, onHitEffects, onDeathEffects,
				onSpawnEffects, onGetHitEffects, randomEffects, ageLocked,
				isBaby);
		this.types = types;
		typeSelector = new RandomSelector<Rabbit.Type>();
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Rabbit rabbit = (Rabbit) l;
		if (types != null) {
			Rabbit.Type type = typeSelector.pickRandomly(types);
			if (type != null) {
				rabbit.setRabbitType(type);
			}
		}
		return l;
	}
}
