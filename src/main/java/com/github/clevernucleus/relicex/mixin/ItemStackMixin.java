package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.relicex.impl.item.ArmorRelicItem;
import com.github.clevernucleus.relicex.impl.item.RelicItem;
import com.github.clevernucleus.relicex.util.Util;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	
	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
	private void onBuild(ItemConvertible item, int count, CallbackInfo info) {
		Item itemIn = item == null ? null : item.asItem();
		ItemStack stack = (ItemStack)(Object)this;
		
		if(itemIn != null && itemIn instanceof RelicItem || itemIn instanceof ArmorRelicItem) {
			Util.createRelic(stack);
		}
	}
	
	@ModifyVariable(method = "getAttributeModifiers", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
	private Multimap<EntityAttribute, EntityAttributeModifier> modifyItemStackModifiers(Multimap<EntityAttribute, EntityAttributeModifier> original, EquipmentSlot slot) {
		ItemStack stack = (ItemStack)(Object)this;
		Item item = stack.getItem();
		
		if(item instanceof ArmorRelicItem) {
			ArmorRelicItem relic = (ArmorRelicItem)item;
			
			return relic.getAttributeModifiers(stack, slot);
		}
		
		return original;
	}
}
