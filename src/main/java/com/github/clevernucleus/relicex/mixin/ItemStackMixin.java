package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.relicex.item.RelicItem;
import com.github.clevernucleus.relicex.util.Util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
	private void onBuild(ItemConvertible item, int count, CallbackInfo info) {
		Item itemIn = item == null ? null : item.asItem();
		ItemStack stack = (ItemStack)(Object)this;
		
		if(itemIn != null && itemIn instanceof RelicItem) {
			Util.createRelic(stack);
		}
	}
}
