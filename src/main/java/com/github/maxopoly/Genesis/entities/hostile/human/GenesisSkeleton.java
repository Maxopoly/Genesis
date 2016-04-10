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

public class GenesisSkeleton extends GenesisHumanEntity {

	private boolean isWitherSkeleton;

	public GenesisSkeleton(String customName, List<CombatEffect> onHitEffects,
			List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, ItemStack helmetSlot,
			ItemStack chestSlot, ItemStack pantSlot, ItemStack bootSlot,
			ItemStack handSlot, double helmetDropChance,
			double chestplateDropChance, double leggingsDropChance,
			double bootsDropChance, double handDropChance, boolean canPickupItems,
			boolean isWitherSkeleton) {
		super(EntityType.SKELETON, customName, onHitEffects, onDeathEffects,
				onSpawnEffects, onGetHitEffects, randomEffects, helmetSlot,
				chestSlot, pantSlot, bootSlot, handSlot, helmetDropChance,
				chestplateDropChance, leggingsDropChance, bootsDropChance,
				handDropChance, canPickupItems);
		this.isWitherSkeleton = isWitherSkeleton;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		Skeleton skele = ((Skeleton) l);
		if (isWitherSkeleton) {
			skele.setSkeletonType(SkeletonType.WITHER);
		} else {
			skele.setSkeletonType(SkeletonType.NORMAL);
		}
		return l;
	}

	/**
	 * @return Whether this skeleton is a wither skeleton or just a normal
	 *         skeleton
	 */
	public boolean isWitherSkeleton() {
		return isWitherSkeleton;
	}
}
