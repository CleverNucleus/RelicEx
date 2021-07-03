package com.github.clevernucleus.relicex.item;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class HealthPotionItem extends Item {
	private final float amount;
	
	public HealthPotionItem(final float amount) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.BREWING));
		this.amount = amount;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(entity == null || !(entity instanceof PlayerEntity)) return;
		
		PlayerEntity player = (PlayerEntity)entity;
		
		if(world.isClient) {
			player.playSound(RelicEx.POTION_USE, SoundCategory.NEUTRAL, 0.75F, 1.0F);
			
			for(int i = 0; i < 6; ++i) {
				double d = RANDOM.nextGaussian() * 0.02D;
	            double e = RANDOM.nextGaussian() * 0.02D;
	            double f = RANDOM.nextGaussian() * 0.02D;
	            
	            world.addParticle(ParticleTypes.HEART, entity.getParticleX(1.0D), entity.getRandomBodyY() + 0.5D, entity.getParticleZ(1.0D), d, e, f);
			}
		} else {
			player.heal(this.amount);
			stack.decrement(1);
		}
	}
}
