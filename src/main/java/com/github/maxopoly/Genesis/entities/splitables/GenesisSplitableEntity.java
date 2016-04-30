package com.github.maxopoly.Genesis.entities.splitables;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public abstract class GenesisSplitableEntity extends GenesisLivingEntity {

	private int childrenCount;
	private int initialSize;
	private boolean recursiveSplit;
	private boolean onlyDropOnSize1;

	public GenesisSplitableEntity(EntityType type, String uniqueTag, String customName,
			Map <EffectCause, List <CombatEffect>> effects, int childrenCount,
			int initialSize, boolean recursiveSplit, boolean onlyDropOnSize1) {
		super(type, uniqueTag, customName, effects);
		this.childrenCount = childrenCount;
		this.initialSize = initialSize;
		this.recursiveSplit = recursiveSplit;
		this.onlyDropOnSize1 = onlyDropOnSize1;
	}
	
	public LivingEntity spawnAt(Location loc) {
		Slime slime = (Slime) super.spawnAt(loc);
		slime.setSize(initialSize);
		return slime;
	}

	

	public int getChildrenCount() {
		return childrenCount;
	}

	public int getInitalSize() {
		return initialSize;
	}
	
	public boolean getRecursiveSplit() {
		return recursiveSplit;
	}
	
	public boolean getDropOnSize1Only() {
		return onlyDropOnSize1;
	}
}
