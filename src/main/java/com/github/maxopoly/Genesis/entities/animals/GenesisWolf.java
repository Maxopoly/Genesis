package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.Drops;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public class GenesisWolf extends GenesisAnimal {

	private RandomSelector<DyeColor> colorSelector;

	private Map<DyeColor, Double> collarColors;

	public GenesisWolf(String uniqueTag, String customName,
			Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby, Map<DyeColor, Double> collarColors) {
		super(EntityType.WOLF, uniqueTag, customName, drops, effects,
				ageLocked, isBaby);
		this.collarColors = collarColors;
		colorSelector = new RandomSelector<DyeColor>();
	}

	public LivingEntity spawnAt(Location loc) {
		Wolf wolf = (Wolf) super.spawnAt(loc);
		if (collarColors != null) {
			DyeColor color = colorSelector.pickRandomly(collarColors);
			if (color != null) {
				wolf.setCollarColor(color);
			}
		}
		return wolf;
	}
}
