package com.github.maxopoly.Genesis.listener;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.Genesis.Genesis;
import com.github.maxopoly.Genesis.GenesisManager;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.misc.Drops;

public class EntityListener implements Listener {

	private GenesisManager manager;

	public EntityListener() {
		this.manager = Genesis.getManager();
	}

	@EventHandler(ignoreCancelled = true)
	public void entitySpawn(EntitySpawnEvent e) {
		GenesisLivingEntity gle = manager.getEntityConfig(e.getEntity()
				.getUniqueId());
		if (gle != null) {
			gle.triggerEffects(EffectCause.SPAWN, e);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void entityDeath(EntityDeathEvent e) {
		GenesisLivingEntity gle = manager.getEntityConfig(e.getEntity()
				.getUniqueId());
		if (gle != null) {
			gle.triggerEffects(EffectCause.DEATH, e);
			List <Drops> drops = gle.getRandomDrop();
			if (drops != null) {
				List <ItemStack> actualDrops = e.getDrops();
				actualDrops.clear();
				for(Drops d : drops) {
					actualDrops.add(d.getRandomDrop());
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void entityHitPlayer(EntityDamageByEntityEvent e) {
		GenesisLivingEntity gle = manager.getEntityConfig(e.getEntity()
				.getUniqueId());
		if (gle != null) {
			if (e.getDamager() instanceof Projectile) {
				gle.triggerEffects(EffectCause.GET_HIT_RANGE, e);
			} else {
				gle.triggerEffects(EffectCause.GET_HIT_MELEE, e);
			}
		}
		Entity damager = e.getDamager();
		boolean projectile = false;
		if (damager instanceof Projectile) {
			if (((Projectile) damager).getShooter() instanceof LivingEntity) {
				damager = (LivingEntity)(((Projectile) damager).getShooter());
				projectile = true;
			}
			else {
				damager = null;
			}
		}
		if (damager != null) {
			GenesisLivingEntity gleDamager = manager.getEntityConfig(e.getDamager().getUniqueId());
			if (gleDamager != null) {
				if (projectile) {
					gle.triggerEffects(EffectCause.HIT_PLAYER_RANGED, e);
				}
				else {
					gle.triggerEffects(EffectCause.HIT_PLAYER_MELEE, e);
				}
			}
		}
	}
}
