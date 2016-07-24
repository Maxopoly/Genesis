package com.github.maxopoly.Genesis.entities.hostile.human;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.misc.Drops;

public class GenesisSkeleton extends GenesisHumanEntity {

	private SkeletonType skeletonType;

	public GenesisSkeleton(String uniqueTag, String customName,
			Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, ItemStack helmetSlot,
			ItemStack chestSlot, ItemStack pantSlot, ItemStack bootSlot,
			ItemStack handSlot, double helmetDropChance,
			double chestplateDropChance, double leggingsDropChance,
			double bootsDropChance, double handDropChance,
			boolean canPickupItems, SkeletonType skeletonType) {
		super(EntityType.SKELETON, uniqueTag, customName, drops, effects,
				helmetSlot, chestSlot, pantSlot, bootSlot, handSlot,
				helmetDropChance, chestplateDropChance, leggingsDropChance,
				bootsDropChance, handDropChance, canPickupItems);
		this.skeletonType = skeletonType;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Skeleton skele = ((Skeleton) l);
		if (skeletonType != null) {
			skele.setSkeletonType(skeletonType);
		}
		return l;
	}

	/**
	 * @return Type of this skeleton (normal/wither/stray)
	 */
	public SkeletonType isWitherSkeleton() {
		return skeletonType;
	}
}
