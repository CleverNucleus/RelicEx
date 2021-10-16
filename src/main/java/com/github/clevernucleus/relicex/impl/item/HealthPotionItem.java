package com.github.clevernucleus.relicex.impl.item;

import java.util.Random;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public final class HealthPotionItem extends Item {
	private final float amount;
	
	public HealthPotionItem(final float amount) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.BREWING));
		
		this.amount = amount;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(entity == null || !(entity instanceof LivingEntity)) return;
		
		final LivingEntity living = (LivingEntity)entity;
		final Random rand = new Random();
		
		if(world.isClient) {
			world.playSound(living.getX(), living.getY(), living.getZ(), RelicEx.POTION_USE_SOUND, SoundCategory.NEUTRAL, 0.75F, 1.0F, true);
			
			for(int i = 0; i < 6; i++) {
				double d = rand.nextGaussian() * 0.02D;
	            double e = rand.nextGaussian() * 0.02D;
	            double f = rand.nextGaussian() * 0.02D;
	            
	            world.addParticle(ParticleTypes.HEART, living.getParticleX(1.0D), living.getRandomBodyY() + 0.5D, living.getParticleZ(1.0D), d, e, f);
			}
		} else {
			living.heal(this.amount);
			stack.decrement(1);
		}
	}
}
