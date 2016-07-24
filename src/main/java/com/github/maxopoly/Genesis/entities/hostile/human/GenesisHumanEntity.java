package com.github.maxopoly.Genesis.entities.hostile.human;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public abstract class GenesisHumanEntity extends GenesisLivingEntity {

	private ItemStack helmetSlot;
	private ItemStack chestSlot;
	private ItemStack pantSlot;
	private ItemStack bootSlot;
	private ItemStack handSlot;

	private double helmetDropChance;
	private double chestPlateDropChance;
	private double pantsDropChance;
	private double bootDropChance;
	private double handDropChance;

	private boolean canPickUpItems;

	public GenesisHumanEntity(EntityType entityType, String uniqueTag,
			String customName, Map<List<Drops>, Double> drops,
			Map<EffectCause, List<CombatEffect>> effects, ItemStack helmetSlot,
			ItemStack chestSlot, ItemStack pantSlot, ItemStack bootSlot,
			ItemStack handSlot, double helmetDropChance,
			double chestplateDropChance, double leggingsDropChance,
			double bootsDropChance, double handDropChance,
			boolean canPickupItems) {
		super(entityType, uniqueTag, customName, drops, effects);
		this.helmetDropChance = helmetDropChance;
		this.chestPlateDropChance = chestplateDropChance;
		this.pantsDropChance = leggingsDropChance;
		this.bootDropChance = bootsDropChance;
		this.handDropChance = handDropChance;
		this.helmetSlot = helmetSlot;
		this.chestSlot = chestSlot;
		this.pantSlot = pantSlot;
		this.bootSlot = bootSlot;
		this.handSlot = handSlot;
		this.handDropChance = handDropChance;
		this.canPickUpItems = canPickupItems;
	}

	public LivingEntity spawnAt(Location loc) {
		LivingEntity l = super.spawnAt(loc);
		EntityEquipment eq = l.getEquipment();
		eq.setHelmet(helmetSlot);
		eq.setHelmetDropChance((float) helmetDropChance);
		eq.setChestplate(chestSlot);
		eq.setChestplateDropChance((float) chestPlateDropChance);
		eq.setLeggings(pantSlot);
		eq.setLeggingsDropChance((float) pantsDropChance);
		eq.setBoots(bootSlot);
		eq.setBootsDropChance((float) bootDropChance);
		return l;
	}

	/**
	 * @return Helmet this entity will wear or null if no helmet is specified
	 */
	public ItemStack getHelmet() {
		return helmetSlot;
	}

	/**
	 * @return Chestplate this entity will wear or null if no chestplate is
	 *         specified
	 */
	public ItemStack getChestplate() {
		return chestSlot;
	}

	/**
	 * @return Leggings this entity will wear or null if no leggings are
	 *         specified
	 */
	public ItemStack getLeggings() {
		return pantSlot;
	}

	/**
	 * @return Boots this entity will wear or null if no boots are specified
	 */
	public ItemStack getBoots() {
		return bootSlot;
	}

	/**
	 * @return Item this entity will hold in its hand or null if no item is
	 *         specified
	 */
	public ItemStack getItemInHand() {
		return handSlot;
	}

	/**
	 * @return Chance for this entity to drop it's boots on death
	 */
	public double getBootDropChance() {
		return bootDropChance;
	}

	/**
	 * @return Chance for this entity to drop it's helmet on death
	 */
	public double getHelmetDropChance() {
		return helmetDropChance;
	}

	/**
	 * @return Chance for this entity to drop it's chestplate on death
	 */
	public double getChestplateDropChance() {
		return chestPlateDropChance;
	}

	/**
	 * @return Chance for this entity to drop it's leggings on death
	 */
	public double getLeggingsDropChance() {
		return pantsDropChance;
	}

	/**
	 * @return Chance for this entity to drop the item it's holding in its hand
	 *         on death
	 */
	public double getItemInHandDropChance() {
		return handDropChance;
	}

	/**
	 * @return Whether this entity can pickup items
	 */
	public boolean canPickUpItems() {
		return canPickUpItems;
	}
}
