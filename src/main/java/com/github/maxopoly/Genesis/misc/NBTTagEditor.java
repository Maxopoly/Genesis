package com.github.maxopoly.Genesis.misc;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class NBTTagEditor {
	public static void addTag(LivingEntity le, String tagName, String tagValue) {
		CraftLivingEntity nmsEntity = (CraftLivingEntity) le;
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.getHandle().c(tag);
		tag.setString(tagName, tagValue);
		nmsEntity.getHandle().a(tag);
	}
	
	public static String getTag(LivingEntity le, String tagName) {
		CraftLivingEntity nmsEntity = (CraftLivingEntity) le;
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.getHandle().c(tag);
		nmsEntity.getHandle().b(tag);
		
	}
}
