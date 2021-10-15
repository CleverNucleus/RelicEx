package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.relicex.impl.item.ArmorRelicItem;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(MobEntity.class)
abstract class MobEntityMixin {
	
	@Inject(method = "tryEquip", at = @At("HEAD"), cancellable = true)
	private void tryEquipRelic(ItemStack equipment, CallbackInfoReturnable<Boolean> info) {
		Item relic = equipment.getItem();
		
		if(relic instanceof ArmorRelicItem) {
			info.setReturnValue(false);
		}
	}
	
	@Inject(method = "equipStack", at = @At("HEAD"), cancellable = true)
	private void equipRelic(EquipmentSlot slot, ItemStack stack, CallbackInfo info) {
		Item relic = stack.getItem();
		
		if(relic instanceof ArmorRelicItem) {
			info.cancel();
		}
	}
	
	@Inject(method = "canPickupItem", at = @At("HEAD"), cancellable = true)
	private void canPickupRelic(ItemStack equipment, CallbackInfoReturnable<Boolean> info) {
		Item relic = equipment.getItem();
		
		if(relic instanceof ArmorRelicItem) {
			info.setReturnValue(false);
		}
	}
}
