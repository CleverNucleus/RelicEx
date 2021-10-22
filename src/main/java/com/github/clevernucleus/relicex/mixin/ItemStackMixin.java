package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.clevernucleus.relicex.impl.item.ArmorRelicItem;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	
	/** TODO slated for removal when PlayerEx fixes modifiers being immutable */
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
