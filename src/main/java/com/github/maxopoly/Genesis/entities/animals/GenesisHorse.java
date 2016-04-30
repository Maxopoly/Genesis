package com.github.maxopoly.Genesis.entities.animals;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.RandomSelector;

public class GenesisHorse extends GenesisAnimal {

	private static RandomSelector<Horse.Color> colorSelector;
	private static RandomSelector<Horse.Style> styleSelector;
	private static RandomSelector<Horse.Variant> variantSelector;

	private Map<Horse.Color, Double> colors;
	private Map<Horse.Style, Double> styles;
	private Map<Horse.Variant, Double> variants;

	private boolean hasChest;
	private int maximumDomestication;

	public GenesisHorse(String uniqueTag, String customName,
			Map<EffectCause, List<CombatEffect>> effects, boolean ageLocked,
			boolean isBaby, Map<Horse.Color, Double> colors,
			Map<Horse.Style, Double> styles,
			Map<Horse.Variant, Double> variants, boolean hasChest,
			int maximumDomestication) {
		super(EntityType.HORSE, uniqueTag, customName, effects, ageLocked,
				isBaby);
		colorSelector = new RandomSelector<Horse.Color>();
		styleSelector = new RandomSelector<Horse.Style>();
		variantSelector = new RandomSelector<Horse.Variant>();
		this.colors = colors;
		this.styles = styles;
		this.variants = variants;
		this.hasChest = hasChest;
		this.maximumDomestication = maximumDomestication;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Horse horse = (Horse) l;
		if (colors != null) {
			Horse.Color color = colorSelector.pickRandomly(colors);
			if (color != null) {
				horse.setColor(color);
			}
		}
		if (styles != null) {
			Horse.Style style = styleSelector.pickRandomly(styles);
			if (style != null) {
				horse.setStyle(style);
			}
		}
		if (variants != null) {
			Horse.Variant variant = variantSelector.pickRandomly(variants);
			if (variant != null) {
				horse.setVariant(variant);
			}
		}
		horse.setCarryingChest(hasChest);
		if (maximumDomestication != -1) {
			horse.setMaxDomestication(maximumDomestication);
		}
		horse.setDomestication(0);
		return l;
	}

	public Map<Horse.Color, Double> getColors() {
		return colors;
	}

	public Map<Horse.Style, Double> getStyles() {
		return styles;
	}

	public Map<Horse.Variant, Double> getVariants() {
		return variants;
	}

	public boolean hasChest() {
		return hasChest;
	}

	public int getMaximumDomestication() {
		return maximumDomestication;
	}
}
