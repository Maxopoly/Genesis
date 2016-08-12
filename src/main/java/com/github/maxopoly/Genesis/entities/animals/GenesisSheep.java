package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.Drops;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public class GenesisSheep extends GenesisAnimal {

	private boolean isSheared;

	private static RandomSelector<DyeColor> colorSelector;

	private Map<DyeColor, Double> colors;

	public GenesisSheep(String uniqueTag, String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked, boolean isBaby, boolean isSheared,
			Map<DyeColor, Double> types) {
		super(EntityType.SHEEP, uniqueTag, customName, drops, effects, ageLocked, isBaby);
		this.isSheared = isSheared;
		this.colors = types;
		colorSelector = new RandomSelector<DyeColor>();
	}

	public LivingEntity spawnAt(Location loc) {
		Sheep sheep = (Sheep) super.spawnAt(loc);
		sheep.setSheared(isSheared);
		sheep.setColor(colorSelector.pickRandomly(colors));
		if (colors != null) {
			DyeColor color = colorSelector.pickRandomly(colors);
			if (color != null) {
				sheep.setColor(color);
			}
		}
		return sheep;
	}

	/**
	 * @return Whether the entity spawned is sheared
	 */
	public boolean isSheared() {
		return isSheared;
	}
}
