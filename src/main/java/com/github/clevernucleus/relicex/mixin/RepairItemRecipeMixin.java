package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.clevernucleus.relicex.item.ArmorRelicItem;

import net.minecraft.item.Item;
import net.minecraft.recipe.RepairItemRecipe;

@Mixin(RepairItemRecipe.class)
abstract class RepairItemRecipeMixin {
	
	@Redirect(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;isDamageable()Z"))
	private boolean relicex_matches(Item item) {
		return item instanceof ArmorRelicItem ? false : item.isDamageable();
	}
}
