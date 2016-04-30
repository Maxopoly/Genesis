package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public class GenesisOcelot extends GenesisAnimal {
	
	private static RandomSelector<Ocelot.Type> typeSelector;
	
	private Map<Ocelot.Type, Double> types;

	public GenesisOcelot(String uniqueTag, String customName,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby, Map<Ocelot.Type, Double> types) {
		super(EntityType.OCELOT, uniqueTag, customName, effects, ageLocked,
				isBaby);
		this.types = types;
		typeSelector = new RandomSelector<Ocelot.Type>();
	}
	
	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Ocelot oce = (Ocelot) l;
		if (types != null) {
			Ocelot.Type type = typeSelector.pickRandomly(types);
			if (type != null) {
				oce.setCatType(type);
			}
		}
		return l;
	}

}
