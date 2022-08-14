package com.github.clevernucleus.relicex.item;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HealthPotionItem extends Item {
	private final float amount;
	
	public HealthPotionItem(final float amount) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.BREWING));
		this.amount = amount;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(entity == null || !(entity instanceof LivingEntity)) return;
		LivingEntity livingEntity = (LivingEntity)entity;
		
		if(world.isClient) {
			world.playSound(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), RelicEx.POTION_USE_SOUND, SoundCategory.NEUTRAL, 0.75F, 1.0F, true);
			Random random = livingEntity.getRandom();
			
			for(int i = 0; i < 6; i++) {
				double d = random.nextGaussian() * 0.02D;
				double e = random.nextGaussian() * 0.02D;
				double f = random.nextGaussian() * 0.02D;
				
				world.addParticle(ParticleTypes.HEART, livingEntity.getParticleX(1.0D), livingEntity.getRandomBodyY() + 0.5D, livingEntity.getParticleZ(1.0D), d, e, f);
			}
		} else {
			livingEntity.heal(this.amount);
			stack.decrement(1);
		}
	}
}
