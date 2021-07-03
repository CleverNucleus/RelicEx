package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin {
	
	@Inject(method = "awardExperience", at = @At("TAIL"))
	private void loot(int amount, CallbackInfo info) {
		EnderDragonEntity entity = (EnderDragonEntity)(Object)this;
		
		if(RelicEx.config().dragonDropsStone) {
			ItemStack stack = new ItemStack(RelicEx.DRAGON_STONE, 1);
			entity.dropStack(stack);
		}
	}
}
