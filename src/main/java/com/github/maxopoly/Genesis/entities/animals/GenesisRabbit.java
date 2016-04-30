package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public class GenesisRabbit extends GenesisAnimal {

	private static RandomSelector<Rabbit.Type> typeSelector;

	private Map<Rabbit.Type, Double> types;

	public GenesisRabbit(String uniqueTag, String customName,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby, Map<Rabbit.Type, Double> types) {
		super(EntityType.RABBIT, uniqueTag, customName, effects, ageLocked,
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
