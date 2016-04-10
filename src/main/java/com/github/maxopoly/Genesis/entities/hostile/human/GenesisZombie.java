package com.github.maxopoly.Genesis.entities.hostile.human;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;

public class GenesisZombie extends GenesisHumanEntity implements HumanGrowable {

	private boolean isChild;

	public GenesisZombie(String customName, List<CombatEffect> onHitEffects,
			List<CombatEffect> onDeathEffects,
			List<CombatEffect> onSpawnEffects,
			List<CombatEffect> onGetHitEffects,
			Map<CombatEffect, Long> randomEffects, ItemStack helmetSlot,
			ItemStack chestSlot, ItemStack pantSlot, ItemStack bootSlot,
			ItemStack handSlot, double helmetDropChance,
			double chestplateDropChance, double leggingsDropChance,
			double bootsDropChance, double handDropChance,
			boolean canPickupItems, boolean child) {
		super(EntityType.ZOMBIE, customName, onHitEffects, onDeathEffects,
				onSpawnEffects, onGetHitEffects, randomEffects, helmetSlot,
				chestSlot, pantSlot, bootSlot, handSlot, helmetDropChance,
				chestplateDropChance, leggingsDropChance, bootsDropChance,
				handDropChance, canPickupItems);
		this.isChild = child;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		((Zombie) l).setBaby(true);
		return l;
	}

	public boolean isChild() {
		return isChild;
	}
}
